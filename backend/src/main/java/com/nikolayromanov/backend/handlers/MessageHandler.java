package com.nikolayromanov.backend.handlers;

import com.nikolayromanov.backend.entities.User;
import com.nikolayromanov.backend.exceptions.TechnicalException;
import com.nikolayromanov.backend.models.Message;
import com.nikolayromanov.backend.models.MessageType;
import com.nikolayromanov.backend.models.ResponseErrors;
import com.nikolayromanov.backend.models.StatusCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    @Autowired
    EchoHandler echoHandler;

    @Autowired
    AuthHandler authHandler;

    public <T> Message handleMessage(Message<T> incomingMessage) {
        logger.info("Received message: {}", incomingMessage);

        String type = incomingMessage.getHeaders().get("type");
        Message message = new Message<>();
        message.setReplyHeader(type);
        MessageType messageType = MessageType.findByValue(type);

        if(messageType == null) {
            Message<T> reply = new Message<>();
            reply.setStatusHeader(StatusCode.ENDPOINT_NOT_FOUND);

            return reply;
        }

        try {
            handleMessageBasedOnType(messageType, incomingMessage.getBody());
            message.setStatusHeader(StatusCode.OK);

            return message;
        } catch (TechnicalException exception) {
            ResponseErrors<String> responseErrors = new ResponseErrors<>();
            responseErrors.getErrors().add(exception.getMessage());
            message.setStatusHeader(StatusCode.INTERNAL_SERVER_ERROR);
            message.setBody(responseErrors);

            return message;
        }
    }

    private <T> void handleMessageBasedOnType(MessageType messageType, T payload) throws TechnicalException {
        switch (messageType) {
            case Echo:
                echoHandler.handleEchoMessage(payload);
                break;
            case AuthRegister:
                authHandler.handleAuthRegister((User) payload);
                break;
            default:
                break;
        }
    }
}
