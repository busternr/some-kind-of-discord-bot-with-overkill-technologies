package com.nikolayromanov.backend.controllers;

import com.nikolayromanov.backend.handlers.EchoHandler;
import com.nikolayromanov.backend.models.annotations.WSController;
import com.nikolayromanov.backend.models.annotations.WSMessageMapping;
import com.nikolayromanov.backend.models.messages.StringMessageBody;

import org.springframework.beans.factory.annotation.Autowired;

@WSController
public class EchoController implements BaseController {
    @Autowired
    EchoHandler echoHandler;

    @Override
    public String getControllerType() {
        return "echo";
    }

    @WSMessageMapping("echo")
    public StringMessageBody echo(StringMessageBody stringMessageBody) {
        return echoHandler.handleEchoMessage(stringMessageBody);
    }
}