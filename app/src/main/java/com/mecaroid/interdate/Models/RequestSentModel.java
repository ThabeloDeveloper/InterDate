package com.mecaroid.interdate.Models;

public class RequestSentModel {
    String user_id;

    public RequestSentModel(String user_id) {
        this.user_id = user_id;
    }

    public RequestSentModel() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
