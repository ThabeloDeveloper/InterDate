package com.mecaroid.interdate.Models;

public class MessagingModel {
    String sender;
    String receiver;
    String message;
    String status;
    String image;

    public MessagingModel(String sender, String receiver, String message, String status, String image, String time) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.status = status;
        this.image = image;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String time;

    public MessagingModel() {
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
