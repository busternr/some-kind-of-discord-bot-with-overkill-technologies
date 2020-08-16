package com.nikolayromanov.backend.controllers;

import com.nikolayromanov.backend.entities.User;
import com.nikolayromanov.backend.handlers.AuthHandler;
import com.nikolayromanov.backend.handlers.MessageHandler;
import com.nikolayromanov.backend.models.Message;
import com.nikolayromanov.backend.models.ResponseErrors;

import com.nikolayromanov.backend.models.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class AuthController {
    @Autowired
    MessageHandler messageHandler;
    @Autowired
    AuthHandler authHandler;

    @MessageMapping("queue/user/auth")
    @SendTo("/queue/user")
    public Message authRegister(Message<User> message) {
        Message<ResponseErrors<ValidationError>> validationErrorsMessage = authHandler.validateAuthRegisterMessage(message);

        if(validationErrorsMessage != null) {
            return validationErrorsMessage;
        }

        return messageHandler.handleMessage(message);
    }
}