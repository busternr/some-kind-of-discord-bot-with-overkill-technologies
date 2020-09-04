package com.nikolayromanov.backend.processors;

import com.nikolayromanov.backend.controllers.BaseController;
import com.nikolayromanov.backend.exceptions.TechnicalException;
import com.nikolayromanov.backend.exceptions.ValidationException;
import com.nikolayromanov.backend.models.MessageObject;
import com.nikolayromanov.backend.models.MessageType;
import com.nikolayromanov.backend.models.RequestBody;
import com.nikolayromanov.backend.models.ResponseBody;
import com.nikolayromanov.backend.models.ResponseErrors;
import com.nikolayromanov.backend.models.StatusCode;
import com.nikolayromanov.backend.models.annotations.WSController;
import com.nikolayromanov.backend.models.annotations.WSMessageMapping;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import com.google.gson.Gson;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class EndpointProcessor {
    private static final Logger logger = LoggerFactory.getLogger(EndpointProcessor.class);

    Map<String, Method> annotatedMethods = new HashMap<>();
    List<BaseController> injectedControllers = new ArrayList<>();

    @Autowired
    AutowireCapableBeanFactory beanFactory;

    @PostConstruct
    private void init() {
        Reflections reflections = new Reflections("com.nikolayromanov.backend");
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(WSController.class);

        for (Class<?> controller : controllers) {
            String controllerName = controller.getSimpleName();

            logger.info("Found WSController: {}", controllerName);

            BaseController baseController = beanFactory.getBean(controllerName,BaseController.class);
            injectedControllers.add(baseController);

            Method[] controllerMethods = controller.getMethods();

            for (Method method : controllerMethods) {
                if (method.isAnnotationPresent(WSMessageMapping.class)) {
                    String methodMessageType = method.getAnnotation(WSMessageMapping.class).value();

                    logger.info("Found WSMessageMapping: {}", methodMessageType);

                    annotatedMethods.put(methodMessageType, method);
                }
            }
        }
    }

    @MessageMapping("queue/user/messages")
    @SendTo("/queue/user")
    public MessageObject<ResponseBody> handleMessage(MessageObject<Map<String, String>> messageObject) {
        logger.info("Received message: {}", messageObject);

        MessageObject<ResponseBody> responseMessage = new MessageObject<>();
        ResponseBody responseBody;
        String messageTypeStr = messageObject.getHeaders().get("type");
        MessageType messageType = MessageType.findByValue(messageTypeStr);

        if(messageType == null) {
            responseMessage.setStatusHeader(StatusCode.ENDPOINT_NOT_FOUND);

            return responseMessage;
        }

        Method method = annotatedMethods.get(messageTypeStr);
        RequestBody body = (RequestBody) this.processMessageBody(messageObject.getBody(), method.getParameterTypes()[0]);

        responseMessage.setReplyHeader(messageTypeStr);

        try {
            Object controller = this.getControllerFromMessageType(messageTypeStr);
            responseBody = (ResponseBody) method.invoke(controller, body);

            responseMessage.setHeaders(messageObject.getHeaders());
            responseMessage.setStatusHeader(StatusCode.OK);
            responseMessage.setBody(responseBody);
        } catch (InvocationTargetException | IllegalAccessException exception) {
            // Because any exception thrown during method.invoke will be wrapped in InvocationTargetException
            return this.resolveException(exception.getCause(), responseMessage);
        }

        return responseMessage;
    }

    private MessageObject<ResponseBody> resolveException(Throwable exception, MessageObject<ResponseBody> responseMessage) {
        if(exception instanceof TechnicalException) {
            logger.warn("Caught TechnicalException exception: {}", exception.getMessage());

            ResponseErrors<String> responseErrors = new ResponseErrors<>();
            responseErrors.getErrors().add(exception.getMessage());
            responseMessage.setStatusHeader(StatusCode.INTERNAL_SERVER_ERROR);
            responseMessage.setBody(responseErrors);

            return responseMessage;
        }
        else if(exception instanceof ValidationException) {
            ValidationException validationException = (ValidationException) exception;

            logger.warn("Caught ValidationException exception: {}", validationException.getValidationErrors());

            responseMessage.setStatusHeader(StatusCode.VALIDATION_ERROR);
            responseMessage.setBody(validationException.getValidationErrors());

            return responseMessage;
        }
        else {
            logger.warn("Caught Exception exception: {}", exception.getMessage());

            ResponseErrors<String> responseErrors = new ResponseErrors<>();
            ArrayList<String> errors = new ArrayList<>();
            errors.add(exception.getMessage());

            responseErrors.setErrors(errors);
            responseMessage.setStatusHeader(StatusCode.UNKNOWN_SERVER_ERROR);
            responseMessage.setBody(responseErrors);

            return responseMessage;
        }
    }
    private <T> T processMessageBody(Map<String, String> messageBody, Class<T> cls) {
        Gson gson = new Gson();

        return gson.fromJson(gson.toJson(messageBody), cls);
    }

    private Object getControllerFromMessageType(String messageType) {
        for (BaseController controller : this.injectedControllers) {
            String type = controller.getControllerType();

            if(messageType.startsWith(type)) {
                return controller;
            }
        }

        return null;
    }
}