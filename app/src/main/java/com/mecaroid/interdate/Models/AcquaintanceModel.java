package com.mecaroid.interdate.Models;

import java.io.Serializable;

public class AcquaintanceModel implements Serializable {

    String user_id;
    String timestamp;

    public AcquaintanceModel() {
    }

    public AcquaintanceModel(String user_id, String timestamp) {
        this.user_id = user_id;
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
