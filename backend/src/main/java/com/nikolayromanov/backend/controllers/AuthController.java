package com.nikolayromanov.backend.controllers;

import com.nikolayromanov.backend.entities.User;
import com.nikolayromanov.backend.exceptions.TechnicalException;
import com.nikolayromanov.backend.exceptions.ValidationException;
import com.nikolayromanov.backend.handlers.AuthHandler;
import com.nikolayromanov.backend.models.annotations.WSController;
import com.nikolayromanov.backend.models.annotations.WSMessageMapping;
import com.nikolayromanov.backend.models.messages.Null;

import org.springframework.beans.factory.annotation.Autowired;

@WSController
public class AuthController implements BaseController {
    @Autowired
    AuthHandler authHandler;

    @Override
    public String getControllerType() {
        return "auth";
    }

    @WSMessageMapping("auth.register")
    public Null authRegister(User user) throws TechnicalException, ValidationException {
        return authHandler.handleAuthRegister(user);
    }
}