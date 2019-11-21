package com.example.beesocial;

import java.util.ArrayList;

public class Event {


    //this is to get the event objects loaded onto the cards on event status fragment


    private String title;
    private String location;
    private String date;
    private String time;
    private ArrayList<User> users;

    public Event() {

    }

    public Event(String title, String location, String date, String time) {
        this.title = title;
        this.location = location;
        this.date = date;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}