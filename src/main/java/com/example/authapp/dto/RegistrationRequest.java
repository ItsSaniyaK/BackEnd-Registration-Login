package com.example.authapp.dto;

public class RegistrationRequest {

    private String name;
    private String password;
    private String email;
    private String phoneNo;

    public RegistrationRequest() {
    }

    public RegistrationRequest(String name, String password, String email, String phoneNo) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phoneNo = phoneNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}