package com.nikolayromanov.backend.models;

import java.util.ArrayList;

public class ResponseErrors<T> {
    public static final String INVALID = "invalid";

    ArrayList<T> errors = new ArrayList<>();

    public ArrayList<T> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<T> errors) {
        this.errors = errors;
    }

    public static Message<ResponseErrors<ValidationError>> formatReplyErrorMessage(ResponseErrors<ValidationError> responseErrors) {
        Message<ResponseErrors<ValidationError>> reply = new Message<>();
        reply.setStatusHeader(StatusCode.VALIDATION_ERROR);
        reply.setBody(responseErrors);

        return reply;
    }
}
