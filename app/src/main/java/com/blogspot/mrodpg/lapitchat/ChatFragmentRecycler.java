package com.blogspot.mrodpg.lapitchat;

/**
 * Created by malavan on 05/08/17.
 */

public class ChatFragmentRecycler {
    String lastmessage;
    String uid;
    String thumb;
    String name;

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
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

    public ChatFragmentRecycler() {

    }

    public ChatFragmentRecycler(String lastmessage, String uid, String thumb, String name) {

        this.lastmessage = lastmessage;
        this.uid = uid;
        this.thumb = thumb;
        this.name = name;
    }
}
