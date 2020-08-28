package com.nikolayromanov.backend.handlers;

import com.nikolayromanov.backend.entities.User;
import com.nikolayromanov.backend.exceptions.TechnicalException;
import com.nikolayromanov.backend.exceptions.ValidationException;
import com.nikolayromanov.backend.models.messages.Null;
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

    public Null handleAuthRegister(User user) throws TechnicalException, ValidationException {
        validateAuthRegisterMessage(user);

        if(userRepository.findByUsername(user.getUsername()) != null) {
            throw new TechnicalException("User with the same username already exists.");
        }
        userRepository.save(user);

        return null;
    }

    public void validateAuthRegisterMessage(User user) throws ValidationException {
        ResponseErrors<ValidationError> responseErrors = new ResponseErrors<>();
        ArrayList<ValidationError> validationErrors = new ArrayList<>();

        if(user.getUsername() == null) {
            validationErrors.add(new ValidationError("username", ResponseErrors.INVALID));
        }
        if(user.getPassword() == null) {
            validationErrors.add(new ValidationError("password", ResponseErrors.INVALID));
        }

        if(validationErrors.size() > 0) {
            responseErrors.setErrors(validationErrors);

            throw new ValidationException(responseErrors);
        }
    }
}
