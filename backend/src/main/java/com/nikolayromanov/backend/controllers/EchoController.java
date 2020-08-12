package com.nikolayromanov.backend.controllers;

import com.nikolayromanov.backend.handlers.MessageHandler;
import com.nikolayromanov.backend.models.Message;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class EchoController {
    @MessageMapping("queue/user/echo")
    @SendTo("/queue/user")
    public Message echo(Message message) {
        return MessageHandler.handleMessage(message);
    }
}