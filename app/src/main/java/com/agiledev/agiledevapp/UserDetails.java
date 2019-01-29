package com.agiledev.agiledevapp;

import java.sql.Date;

/**
 * Created by glees on 28/01/2019.
 */

public class UserDetails {
    int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    String forename;
    public String getForename() {
        return forename;
    }
    public void setForename(String forename) {
        this.forename = forename;
    }

    String surname;
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    Date dob;
    public Date getDob() {
        return dob;
    }
    public void setDob(Date dob) {
        this.dob = dob;
    }

    String username;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
