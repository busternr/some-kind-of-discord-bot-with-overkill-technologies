package com.nikolayromanov.backend.models;

import java.util.HashMap;
import java.util.Map;

public class Message<T> {
    private Map<String,String> headers = new HashMap<>();
    private T body;

    public Message() {
    }

    public Message(Map<String, String> headers, T body) {
        this.headers = headers;
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Message{" +
                "headers=" + headers +
                ", body=" + body +
                '}';
    }

    public void setReplyHeader(String type) {
        headers.put("type", type + ".reply");
    }

    public void setStatusHeader(StatusCode statusCode) {
        headers.put("statusCode", statusCode.getValue());
    }
}
