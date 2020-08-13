package com.nikolayromanov.backend.config;

import com.nikolayromanov.platform.models.NatsConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Scope("singleton")
public class Context {
    private static final Logger logger = LoggerFactory.getLogger(Context.class);

    private NatsConnection natsConnection;

    public Context() {
        try {
            this.natsConnection = new NatsConnection();
        } catch (IOException | InterruptedException exception) {
            logger.error("Cannot create NatsConnection: {}", exception.getMessage());
        }

    }

    public NatsConnection getNatsConnection() {
        return natsConnection;
    }
}
