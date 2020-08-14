package com.nikolayromanov.platform.models;

import java.util.Objects;

public class ServiceData {
    private final ServiceType type;
    private final String name;
    private final ServiceStatus status;
    private final String version;
    private boolean awaitsPingReply;

    public ServiceData(ServiceType type, String name, ServiceStatus status, String version) {
        this.type = type;
        this.name = name;
        this.status = status;
        this.version = version;
        this.awaitsPingReply = false;
    }

    public ServiceType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public ServiceStatus getStatus() {
        return status;
    }

    public String getVersion() {
        return version;
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
                ", status=" + status +
                ", version='" + version + '\'' +
                ", awaitsPingReply=" + awaitsPingReply +
                '}';
    }
}
