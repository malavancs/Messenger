package com.blogspot.mrodpg.lapitchat;

/**
 * Created by malavan on 05/08/17.
 */

public class ChatClass {
    String msg;
    String time;
    String seen;
    String who;





    public ChatClass() {

    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public ChatClass(String msg, String time, String seen, String who) {
        this.msg = msg;
        this.time = time;
        this.seen = seen;
       this.who = who;

    }

    public String getMsg() {

        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }
}
