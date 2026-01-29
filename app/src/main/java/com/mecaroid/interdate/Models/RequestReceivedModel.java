package com.mecaroid.interdate.Models;

public class RequestReceivedModel {
    String user_id;

    public RequestReceivedModel(String user_id) {
        this.user_id = user_id;
    }

    public RequestReceivedModel() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
