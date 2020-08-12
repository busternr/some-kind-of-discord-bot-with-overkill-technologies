package com.nikolayromanov.platform.models;

import io.nats.client.Connection;
import io.nats.client.Nats;

import java.io.IOException;

public class NatsConnection {
    private Connection connection;

    public NatsConnection() throws IOException, InterruptedException {
        connection = Nats.connect("nats://localhost:4222");
    }

    public Connection getConnection() {
        return connection;
    }
}
