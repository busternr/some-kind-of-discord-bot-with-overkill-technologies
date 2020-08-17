package com.nikolayromanov.backend.controllers;

import com.nikolayromanov.backend.handlers.MessageHandler;
import com.nikolayromanov.backend.models.Message;
import com.nikolayromanov.backend.models.MessageObject;

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
    public MessageObject<String> echo(MessageObject<String> messageObject) {
        Message<String, String> message = new Message<>(messageObject, new MessageObject<String>());

        return messageHandler.handleMessage(message);
    }
}