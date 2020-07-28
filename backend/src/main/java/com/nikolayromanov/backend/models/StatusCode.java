package com.nikolayromanov.backend.models;

public enum StatusCode {
    OK("OK"),
    ENDPOINT_NOT_FOUND("ENDPOINT_NOT_FOUND");

    private final String value;

    StatusCode(String statusCode) {
        this.value = statusCode;
    }

    public String getValue() {
        return value;
    }
}
