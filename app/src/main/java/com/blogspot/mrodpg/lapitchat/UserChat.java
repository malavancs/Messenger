package com.blogspot.mrodpg.lapitchat;

/**
 * Created by malavan on 05/08/17.
 */

public class UserChat  {
    String name;

    public UserChat() {

    }

    public UserChat(String name, String lastmessage) {
        this.name = name;
        this.lastmessage = lastmessage;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    String lastmessage;
}
