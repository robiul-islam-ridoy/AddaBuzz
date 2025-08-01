package com.example.addabuzz;

public class Users {
    String profileImage, userName, userEmail, userId, userPassword, userLastMessage, userStatus;

   public Users(){}


    //Getters
    public String getProfileImage() {
        return profileImage;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserLastMessage() {
        return userLastMessage;
    }

    public String getUserStatus() {
        return userStatus;
    }

    //Setters
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserLastMessage(String userLastMessage) {
        this.userLastMessage = userLastMessage;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
}
