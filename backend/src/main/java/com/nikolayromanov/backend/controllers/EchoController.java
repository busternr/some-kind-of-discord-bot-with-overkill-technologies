package com.nikolayromanov.backend.controllers;

import com.nikolayromanov.backend.handlers.MessageHandler;
import com.nikolayromanov.backend.models.Message;
import com.nikolayromanov.backend.models.annotations.EchoMessageMapping;
import com.nikolayromanov.backend.models.messages.StringMessageBody;
import com.nikolayromanov.backend.models.MessageObject;
import com.nikolayromanov.backend.models.ResponseBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class EchoController {
    @Autowired
    MessageHandler messageHandler;

    @EchoMessageMapping
    public MessageObject<ResponseBody> echo(MessageObject<StringMessageBody> messageObject) {
        Message<StringMessageBody, StringMessageBody> message = new Message<>(messageObject, new MessageObject<>());

        return messageHandler.handleMessage(message);
    }
}