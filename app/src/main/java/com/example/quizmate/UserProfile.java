package com.example.quizmate;

public class UserProfile {
    private String name;
    private String email;
    private String phoneNumber;

    public UserProfile() {
    }

    public UserProfile(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}