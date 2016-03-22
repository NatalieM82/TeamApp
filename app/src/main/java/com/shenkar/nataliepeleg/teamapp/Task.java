package com.shenkar.nataliepeleg.teamapp;

/**
 * Created by NatalieMenahem on 20/03/2016.
 */
public class Task {
    public String text;
    public String id;
    public Boolean done;
    public String status;

    public Task(String text, String id, Boolean done, String status) {
        this.text = text;
        this.id = id;
        this.done = done;
        this.status = status;
    }

    public String getId(){ return id; }

    public String getText(){
        return text;
    }

    public String getStatus() { return status; }

    public Boolean getDone(){ return done; }
}
