package com.nikolayromanov.backend.models;

public enum MessageType {
    EchoHi("echo.hi"),
    AuthRegister("auth.register");

    private final String value;

    MessageType(String messageType) {
        this.value = messageType;
    }

    public String getValue() {
        return value;
    }

    public static MessageType findByValue(final String value) {
        for (MessageType e : MessageType.values()) {
            if (e.getValue().equalsIgnoreCase(value)) {
                return e;
            }
        }

        return null;
    }
}
