package com.nikolayromanov.backend.models;

public class Message<T extends RequestBody,S extends ResponseBody> {
    MessageObject<T> requestMessage;
    MessageObject<S> replyMessage;

    public Message(MessageObject<T> requestMessage, MessageObject<S> replyMessage) {
        this.requestMessage = requestMessage;
        this.replyMessage = replyMessage;
    }

    public MessageObject<T> getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(MessageObject<T> requestMessage) {
        this.requestMessage = requestMessage;
    }

    public MessageObject<S> getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(MessageObject<S> replyMessage) {
        this.replyMessage = replyMessage;
    }
}
