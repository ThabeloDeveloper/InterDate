package com.mecaroid.interdate.Models;

public class FriendsCircledModel {

    String user_id, timestamp;

    public FriendsCircledModel() {
    }

    public FriendsCircledModel(String user_id,String timestamp) {
        this.user_id = user_id;
        this.timestamp = timestamp;
    }
    public String getTimestamp(){
        return timestamp;
    }
    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String User_id) {
        this.user_id = User_id;
    }


}
