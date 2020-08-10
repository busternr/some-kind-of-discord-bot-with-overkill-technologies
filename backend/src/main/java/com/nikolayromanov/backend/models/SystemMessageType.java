package com.nikolayromanov.backend.models;

public enum SystemMessageType {
    MonitorRegisterService("monitor.registerService"),
    MonitorPing("monitor.ping"),
    MonitorPingReply("monitor.ping.reply"),
    MonitorUnregisterService("monitor.unregisterService");

    private final String value;

    SystemMessageType(String messageType) {
        this.value = messageType;
    }

    public String getValue() {
        return value;
    }

    public static SystemMessageType findByValue(final String value) {
        for (SystemMessageType e : SystemMessageType.values()) {
            if (e.getValue().equalsIgnoreCase(value))
                return e;
        }
        return null;
    }
}
