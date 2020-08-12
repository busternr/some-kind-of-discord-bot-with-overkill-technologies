package com.nikolayromanov.backend.handlers;

import com.nikolayromanov.backend.models.Message;
import com.nikolayromanov.backend.models.MessageType;
import com.nikolayromanov.backend.models.Status;
import com.nikolayromanov.backend.models.StatusCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    public static Message handleMessage(Message incomingMessage) {
        logger.info("Received message: {}", incomingMessage);

        String type = incomingMessage.getHeaders().get("type");
        Map<String, String> headers = new HashMap<>();
        headers.put("type", type + ".reply");

        MessageType messageType = MessageType.findByValue(type);
        if(messageType == null) {
            return new Message(new Status(StatusCode.ENDPOINT_NOT_FOUND, ""));
        }

        return new Message(headers, handleMessageBasedOnType(messageType, incomingMessage.getBody()));
    }

    private static String handleMessageBasedOnType(MessageType messageType, Object payload) {
        switch (messageType) {
            case Echo:
                return handleEchoMessage(payload);
            default:
                return null;
        }
    }

    private static String handleEchoMessage(Object payload) {
        return "Hi, " + payload;
    }
}
