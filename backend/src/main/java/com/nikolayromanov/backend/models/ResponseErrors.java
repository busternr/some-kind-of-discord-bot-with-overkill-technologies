package com.nikolayromanov.backend.models;

import java.util.ArrayList;

public class ResponseErrors<T> implements ResponseBody {
    public static final String INVALID = "invalid";

    ArrayList<T> errors = new ArrayList<>();

    public ArrayList<T> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<T> errors) {
        this.errors = errors;
    }

    public static MessageObject<ResponseErrors<ValidationError>> formatReplyErrorMessage(ResponseErrors<ValidationError> responseErrors) {
        MessageObject<ResponseErrors<ValidationError>> reply = new MessageObject<>();
        reply.setStatusHeader(StatusCode.VALIDATION_ERROR);
        reply.setBody(responseErrors);

        return reply;
    }

    @Override
    public String toString() {
        return "ResponseErrors{" +
                "errors=" + errors +
                '}';
    }
}
