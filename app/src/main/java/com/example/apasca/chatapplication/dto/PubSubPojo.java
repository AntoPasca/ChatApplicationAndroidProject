package com.example.apasca.chatapplication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PubSubPojo {
    private String sender;
    private String message;

    public PubSubPojo(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}