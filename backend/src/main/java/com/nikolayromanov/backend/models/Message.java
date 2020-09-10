package com.nikolayromanov.backend.models;

import java.util.HashMap;
import java.util.Map;

public class Message {
    private Map<String,String> headers = new HashMap<>();
    private Object body;

    public Message() {
    }

    public Message(Object body) {
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

    public void setHeader(String header, String value) {
        headers.put(header, value);
    }

    public void setReplyHeader(String type) {
        this.setHeader("type", type + ".reply");
    }

    public void setStatusHeader(StatusCode statusCode) {
        this.setHeader("statusCode", statusCode.getValue());
    }
}
