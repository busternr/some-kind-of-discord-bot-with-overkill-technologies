package com.nikolayromanov.backend.exceptions;

import com.nikolayromanov.backend.models.ResponseErrors;
import com.nikolayromanov.backend.models.ValidationError;

public class ValidationException extends Exception {
    private final ResponseErrors<ValidationError> validationErrors;

    public ValidationException(ResponseErrors<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public ResponseErrors<ValidationError> getValidationErrors() {
        return validationErrors;
    }
}