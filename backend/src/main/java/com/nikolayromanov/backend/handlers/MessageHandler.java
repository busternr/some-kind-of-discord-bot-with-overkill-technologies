package com.nikolayromanov.backend.handlers;

import com.nikolayromanov.backend.controllers.EchoController;
import com.nikolayromanov.backend.models.Message;
import com.nikolayromanov.backend.models.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(EchoController.class);

    public static Message handleMessage(Message incomingMessage) {
        logger.info("Received message: {}", incomingMessage);

        String type = incomingMessage.getHeaders().get("type");
        Map<String, String> headers = new HashMap<>();
        headers.put("type", type + ".reply");

        // TODO: Handle java.lang.NullPointerException if type does not exist
        return new Message(headers, handleMessageBasedOnType(MessageType.findByValue(type), incomingMessage.getBody()));

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
