package com.nikolayromanov.serviceMonitor.models;

public enum ServiceType {
    BackendCore("BACKEND_CORE");

    private final String value;

    ServiceType(String value_) {
        this.value = value_;
    }

    public String getValue() {
        return this.value;
    }
}
