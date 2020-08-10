package com.nikolayromanov.backend.models;

import java.util.Objects;

public class ServiceData {
    private final ServiceType type;
    private final String name;
    private final String version;
    private final ServiceStatus status;
    private boolean awaitsPingReply;

    public ServiceData(ServiceType type, String name, String version, ServiceStatus status) {
        this.type = type;
        this.name = name;
        this.version = version;
        this.status = status;
        this.awaitsPingReply = false;
    }

    public boolean isAwaitsPingReply() {
        return awaitsPingReply;
    }

    public void setAwaitsPingReply(boolean awaitsPingReply) {
        this.awaitsPingReply = awaitsPingReply;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceData that = (ServiceData) o;
        return type == that.type &&
                name.equals(that.name) &&
                version.equals(that.version) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, version, status);
    }

    @Override
    public String toString() {
        return "ServiceData{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", status=" + status +
                ", awaitsPingReply=" + awaitsPingReply +
                '}';
    }
}
