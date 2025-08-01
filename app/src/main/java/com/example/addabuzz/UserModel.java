package com.example.addabuzz;

public class UserModel {
    public String fullName, email, profileImage;

    public UserModel() {} // Required for Firebase

    public UserModel(String fullName, String email, String profileImage) {
        this.fullName = fullName;
        this.email = email;
        this.profileImage = profileImage;
    }
}
