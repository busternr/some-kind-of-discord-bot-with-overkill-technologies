package com.nikolayromanov.backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class EchoController {
    private static final Logger logger = LoggerFactory.getLogger(EchoController.class);
    private final SimpMessagingTemplate simpMessagingTemplate;

    public EchoController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("echo")
    public void echo(String message) {
        logger.info("echo message: {}", message);
        this.simpMessagingTemplate.convertAndSend("/queue/messages", message);
    }
}