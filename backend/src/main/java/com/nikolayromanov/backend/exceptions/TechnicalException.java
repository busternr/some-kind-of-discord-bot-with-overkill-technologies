package com.nikolayromanov.backend.exceptions;

public class TechnicalException extends Exception {
    public static final String SERVICE_ALREADY_REGISTERED = "Service is already registered and monitored.";
    public static final String SERVICE_NOT_REGISTERED = "Service is not registered and cannot be removed from the monitor list.";

    private final String message;

    public TechnicalException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}