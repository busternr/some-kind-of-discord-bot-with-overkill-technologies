package com.nikolayromanov.backend.models;

public enum ServiceType {
    BackendCore("Backend Core");

    private final String value;

    ServiceType(String value_) {
        this.value = value_;
    }

    public String getValue() {
        return this.value;
    }
}
