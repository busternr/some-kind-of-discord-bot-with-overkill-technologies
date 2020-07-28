package com.nikolayromanov.backend.models;

import java.util.Map;

public class Message {
    private Map<String,String> headers;
    private Object body;

    public Message(Map<String, String> headers, Object body) {
        this.headers = headers;
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Message{" +
                "headers=" + headers +
                ", body=" + body +
                '}';
    }
}
