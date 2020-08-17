package com.nikolayromanov.backend.handlers;

import com.nikolayromanov.backend.entities.User;
import com.nikolayromanov.backend.exceptions.TechnicalException;
import com.nikolayromanov.backend.models.Message;
import com.nikolayromanov.backend.models.MessageType;
import com.nikolayromanov.backend.models.MessageObject;
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

    // TODO: Fix the mess with missing MessageObject and Message types!
    public <T,S> MessageObject<S> handleMessage(Message<T,S> incomingMessage) {
        logger.info("Received message: {}", incomingMessage);

        MessageObject<T> incomingRequestMessage = new MessageObject<T>(incomingMessage.getRequestMessage());

        String type = incomingRequestMessage.getHeaders().get("type");
        MessageObject replyMessage = new MessageObject<>();
        replyMessage.setReplyHeader(type);
        MessageType messageType = MessageType.findByValue(type);

        if(messageType == null) {
            MessageObject<S> reply = new MessageObject<>();
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
    }

    private <T,S> S handleMessageBasedOnType(MessageType messageType, T payload) throws TechnicalException {
        switch (messageType) {
            case Echo:
                return (S) echoHandler.handleEchoMessage((String) payload);
            case AuthRegister:
                return (S) authHandler.handleAuthRegister((User) payload);
            default:
                return null;
        }
    }
}
