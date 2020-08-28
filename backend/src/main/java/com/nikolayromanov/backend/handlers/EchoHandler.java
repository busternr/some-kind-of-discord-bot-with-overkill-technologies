package com.nikolayromanov.backend.handlers;

import com.nikolayromanov.backend.models.messages.StringMessageBody;

import org.springframework.stereotype.Component;

@Component
public class EchoHandler {
    public StringMessageBody handleEchoMessage(StringMessageBody message) {
        return new StringMessageBody("Hi, " + message.getMessage());
    }
}
