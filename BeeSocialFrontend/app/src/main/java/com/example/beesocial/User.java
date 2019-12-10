package com.example.beesocial;

class User {

    private String userID;
    private String firstName;
    private String lastName;

    String getUserID() {
        return userID;
    }

    void setUserID(String userID) {
        this.userID = userID;
    }

    String getFirstName() {
        return firstName;
    }

    void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    String getLastName() {
        return lastName;
    }

    void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

