package com.nikolayromanov.backend.config;

import com.nikolayromanov.backend.controllers.EchoController;
import com.nikolayromanov.backend.controllers.AuthController;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfiguration {
    @Bean()
    public EchoController EchoController() {
        return new EchoController();
    }

    @Bean()
    public AuthController AuthController() {
        return new AuthController();
    }
}
