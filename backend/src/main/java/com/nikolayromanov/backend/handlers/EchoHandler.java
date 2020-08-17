package com.nikolayromanov.backend.handlers;

import org.springframework.stereotype.Component;

@Component
public class EchoHandler {
    public String handleEchoMessage(String message) {
        return "Hi, " + message;
    }
}
