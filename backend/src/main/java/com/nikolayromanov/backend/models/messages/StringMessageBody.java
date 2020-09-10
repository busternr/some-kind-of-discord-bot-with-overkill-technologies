package com.nikolayromanov.backend.models.messages;

import java.util.Map;

public class StringMessageBody {
    private String message;

    public StringMessageBody() {
    }

    public StringMessageBody(String message) {
        this.message = message;
    }

    public StringMessageBody(Map<String, String> body) {
        this.message = body.get("message");
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "StringMessageBody{" +
                "message='" + message + '\'' +
                '}';
    }
}
