package com.nikolayromanov.backend.controllers;

import com.nikolayromanov.backend.entities.User;
import com.nikolayromanov.backend.handlers.AuthHandler;
import com.nikolayromanov.backend.handlers.MessageHandler;
import com.nikolayromanov.backend.models.Message;
import com.nikolayromanov.backend.models.MessageObject;
import com.nikolayromanov.backend.models.ResponseErrors;
import com.nikolayromanov.backend.models.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import javax.lang.model.type.NullType;

@Controller
public class AuthController {
    @Autowired
    MessageHandler messageHandler;
    @Autowired
    AuthHandler authHandler;

    @MessageMapping("queue/user/auth")
    @SendTo("/queue/user")
    // TODO: Fix the mess with missing MessageObject and Message types!
    public MessageObject authRegister(MessageObject<User> messageObject) {
        Message<User, NullType> message = new Message<>(messageObject, new MessageObject<NullType>());

        MessageObject<ResponseErrors<ValidationError>> validationErrorsReplyMessage = authHandler.validateAuthRegisterMessage(messageObject);

        if(validationErrorsReplyMessage != null) {
            return validationErrorsReplyMessage;
        }

        return messageHandler.handleMessage(message);
    }
}