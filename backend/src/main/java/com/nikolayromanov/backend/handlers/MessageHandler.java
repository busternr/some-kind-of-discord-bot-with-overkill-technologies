package com.nikolayromanov.backend.handlers;

import com.nikolayromanov.backend.entities.User;
import com.nikolayromanov.backend.exceptions.TechnicalException;
import com.nikolayromanov.backend.exceptions.ValidationException;
import com.nikolayromanov.backend.models.messages.StringMessageBody;
import com.nikolayromanov.backend.models.Message;
import com.nikolayromanov.backend.models.MessageObject;
import com.nikolayromanov.backend.models.RequestBody;
import com.nikolayromanov.backend.models.ResponseBody;
import com.nikolayromanov.backend.models.MessageType;
import com.nikolayromanov.backend.models.ResponseErrors;
import com.nikolayromanov.backend.models.StatusCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    @Autowired
    EchoHandler echoHandler;

    @Autowired
    AuthHandler authHandler;

    public <T extends RequestBody,S extends ResponseBody> MessageObject<ResponseBody> handleMessage(Message<T,S> incomingMessage) {
        MessageObject<T> incomingRequestMessage = new MessageObject<T>(incomingMessage.getRequestMessage());

        logger.info("Received message: {}", incomingMessage.getRequestMessage());

        String type = incomingRequestMessage.getHeaders().get("type");
        MessageObject<ResponseBody> replyMessage = new MessageObject<>();
        replyMessage.setReplyHeader(type);
        MessageType messageType = MessageType.findByValue(type);

        if(messageType == null) {
            MessageObject<ResponseBody> reply = new MessageObject<>();
            reply.setStatusHeader(StatusCode.ENDPOINT_NOT_FOUND);

            return reply;
        }

        try {
            replyMessage.setBody(handleMessageBasedOnType(messageType, incomingRequestMessage.getBody()));
            replyMessage.setStatusHeader(StatusCode.OK);

            return replyMessage;
        } catch (TechnicalException exception) {
            ResponseErrors<String> responseErrors = new ResponseErrors<>();
            responseErrors.getErrors().add(exception.getMessage());
            replyMessage.setStatusHeader(StatusCode.INTERNAL_SERVER_ERROR);
            replyMessage.setBody(responseErrors);

            return replyMessage;
        }
        catch (ValidationException exception) {
            replyMessage.setBody(exception.getValidationErrors());

            return replyMessage;
        } catch (Exception exception) {
            ResponseErrors<String> responseErrors = new ResponseErrors<>();
            replyMessage.setStatusHeader(StatusCode.UNKNOWN_SERVER_ERROR);
            replyMessage.setBody(responseErrors);

            return replyMessage;
        }
    }

    private ResponseBody handleMessageBasedOnType(MessageType messageType, RequestBody payload) throws TechnicalException, ValidationException {
        switch (messageType) {
            case Echo:
                return echoHandler.handleEchoMessage((StringMessageBody) payload);
            case AuthRegister:
                return authHandler.handleAuthRegister((User) payload);
            default:
                return null;
        }
    }
}
