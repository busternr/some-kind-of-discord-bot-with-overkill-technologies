package com.nikolayromanov.platform.models;

public enum ServiceStatus {
    Initializing("Initializing"),
    Ready("Ready"),
    Error("Error");

    private final String value;

    ServiceStatus(String value_) {
        this.value = value_;
    }

    public String getValue() {
        return this.value;
    }
}

