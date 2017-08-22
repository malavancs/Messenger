package com.blogspot.mrodpg.lapitchat;

/**
 * Created by malavan on 06/08/17.
 */

public class RequestClass {

    String uid;
    String thumb;

    public RequestClass() {
    }

    public RequestClass(String uid, String thumb, String name) {

        this.uid = uid;
        this.thumb = thumb;
        this.name = name;
    }

    public String getUid() {

        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;


}
