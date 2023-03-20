package com.codespacepro.loginappfirebase.Models;

public class Users {
    String username, fullname, email, password, gender, dob;
    String profile;

    public Users(String username, String fullname, String email, String password, String gender, String dob) {
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.dob = dob;
    }

    public Users(String username, String fullname, String email, String password, String gender, String dob, String profile) {
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.dob = dob;
        this.profile = profile;
    }

    public Users() {
    }

    public Users(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
