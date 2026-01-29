package com.mecaroid.interdate.Models;

import java.io.Serializable;

public class ImageModel implements Serializable {

    String Uri,privacy,Uid,time,onMind;

    public ImageModel() {
    }

    public ImageModel(String uri, String time, String uid, String privacy,String onMind) {
        Uri = uri;
        this.time = time;
        Uid = uid;
        this.privacy = privacy;
        this.onMind = onMind;
    }

    public String getOnMind() {
        return onMind;
    }

    public void setOnMind(String onMind) {
        this.onMind = onMind;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getUri() {
        return Uri;
    }

    public void setUri(String uri) {
        Uri = uri;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }
}
