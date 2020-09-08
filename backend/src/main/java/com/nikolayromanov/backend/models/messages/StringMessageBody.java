package com.nikolayromanov.backend.models.messages;

public class StringMessageBody {
    private String message;

    public StringMessageBody() {
    }

    public StringMessageBody(String message) {
        this.message = message;
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
