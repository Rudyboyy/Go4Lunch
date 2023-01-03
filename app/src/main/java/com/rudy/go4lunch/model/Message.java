package com.rudy.go4lunch.model;

public class Message {

    User userSender;
    long creationTimeStamp;
    String message;
    String urlImage;

    public Message() {
    }

    public Message(String message, User user, long creationTimeStamp) {
        this.message = message;
        this.userSender = user;
        this.urlImage = null;
        this.creationTimeStamp = creationTimeStamp;
    }

    public User getUserSender() {
        return userSender;
    }

    public void setUserSender(User userSender) {
        this.userSender = userSender;
    }

    public long getCreationTimeStamp() {
        return creationTimeStamp;
    }

    public void setCreationTimeStamp(long creationTimeStamp) {
        this.creationTimeStamp = creationTimeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
