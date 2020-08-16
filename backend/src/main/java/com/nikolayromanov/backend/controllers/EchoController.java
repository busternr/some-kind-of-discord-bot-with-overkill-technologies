package com.nikolayromanov.backend.controllers;

import com.nikolayromanov.backend.handlers.MessageHandler;
import com.nikolayromanov.backend.models.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class EchoController {
    @Autowired
    MessageHandler messageHandler;

    @MessageMapping("queue/user/echo")
    @SendTo("/queue/user")
    public Message<String> echo(Message message) {
        return messageHandler.handleMessage(message);
    }
}