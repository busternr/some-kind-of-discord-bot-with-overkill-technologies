package com.nikolayromanov.backend.handlers;

import com.nikolayromanov.backend.entities.User;
import com.nikolayromanov.backend.exceptions.TechnicalException;
import com.nikolayromanov.backend.models.Message;
import com.nikolayromanov.backend.models.ResponseErrors;
import com.nikolayromanov.backend.models.ValidationError;
import com.nikolayromanov.backend.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class AuthHandler {
    @Autowired
    UserRepository userRepository;

    public void handleAuthRegister(User user) throws TechnicalException {
        userRepository.save(user);

        if(userRepository.findByUsername(user.getUsername()) != null) {
            throw new TechnicalException("User with the same username already exists.");
        }
    }

    public Message<ResponseErrors<ValidationError>> validateAuthRegisterMessage(Message<User> message) {
        ResponseErrors<ValidationError> responseErrors = new ResponseErrors<>();
        ArrayList<ValidationError> validationErrors = new ArrayList<>();

        if(message.getBody().getUsername() == null) {
            validationErrors.add(new ValidationError("username", ResponseErrors.INVALID));
        }
        if(message.getBody().getPassword() == null) {
            validationErrors.add(new ValidationError("password", ResponseErrors.INVALID));
        }

        if(validationErrors.size() > 0) {
            responseErrors.setErrors(validationErrors);

            return ResponseErrors.formatReplyErrorMessage(responseErrors);
        }

        return null;
    }
}
