package com.example.ITBCloggerdemo.model;

import java.util.UUID;

public class Client1 {

    private UUID id;

    private  String username;

    private  String email;

    private String userType;

    private String logNumber;

    public Client1(UUID id, String username, String email, String userType, String logNumber) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.userType = userType;
        this.logNumber = logNumber;
    }



    public Client1(){};

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLogNumber() {
        return logNumber;
    }

    public void setLogNumber(String logNumber) {
        this.logNumber = logNumber;
    }
}
