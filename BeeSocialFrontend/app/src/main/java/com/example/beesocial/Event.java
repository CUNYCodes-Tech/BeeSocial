package com.example.beesocial;

import java.util.ArrayList;

public class Event {


    //this is to get the event objects loaded onto the cards on event status fragment

    private String eventID;
    private String title;
    private String location;
    private String date;
    private String time;
    private ArrayList<User> users;

    Event() {

    }

//    public Event(String title, String location, String date, String time) {
//        this.title = title;
//        this.location = location;
//        this.date = date;
//        this.time = time;
//    }

    String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    String getDate() {
        return date;
    }

    String getTime() {
        return time;
    }

    void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    void setDate(String date) {
        this.date = date;
    }

//    public void setTime(String time) {
//        this.time = time;
//    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    String getEventID() {
        return eventID;
    }

    void setEventID(String eventID) {
        this.eventID = eventID;
    }
}