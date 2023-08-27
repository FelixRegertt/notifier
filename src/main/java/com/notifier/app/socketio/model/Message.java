package com.notifier.app.socketio.model;

import lombok.Data;

@Data
public class Message {
    private MessageType type;
    private String message;

    public Message() {
    }

    public Message(MessageType type, String message) {
        this.type = type;
        this.message = message;
    }
}

