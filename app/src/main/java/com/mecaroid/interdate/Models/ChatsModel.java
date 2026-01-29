package com.mecaroid.interdate.Models;

public class ChatsModel {
    String sender,receiver,message,status,image;

    public ChatsModel() {
    }

    public ChatsModel(String sender, String receiver, String message, String status, String image) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.status = status;
        this.image = image;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
