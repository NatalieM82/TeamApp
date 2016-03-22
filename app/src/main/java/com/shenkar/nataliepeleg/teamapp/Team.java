package com.shenkar.nataliepeleg.teamapp;

/**
 * Created by NatalieMenahem on 17/03/2016.
 */
public class Team {

    public String name;
    public String id;

    public Team(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }
}
