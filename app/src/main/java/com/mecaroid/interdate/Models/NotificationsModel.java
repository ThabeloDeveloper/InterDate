package com.mecaroid.interdate.Models;

public class NotificationsModel {

    String title,body, type,uid;

    public NotificationsModel() {
    }

    public NotificationsModel(String title, String body, String type, String uid) {
        this.title = title;
        this.body = body;
        this.type = type;
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
