package com.nikolayromanov.backend.models;

public enum StatusCode {
    OK("OK"),
    ENDPOINT_NOT_FOUND("ENDPOINT_NOT_FOUND"),
    VALIDATION_ERROR("VALIDATION_ERROR"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR"),
    UNKNOWN_SERVER_ERROR("UNKNOWN_SERVER_ERROR");

    private final String value;

    StatusCode(String statusCode) {
        this.value = statusCode;
    }

    public String getValue() {
        return value;
    }
}
