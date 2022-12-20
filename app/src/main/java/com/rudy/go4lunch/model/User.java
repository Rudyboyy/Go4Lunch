package com.rudy.go4lunch.model;

import androidx.annotation.Nullable;

public class User {

    private String uid;
    private String username;
    @Nullable
    private String urlPicture;
    private String bookedRestaurant;
    private String bookedRestaurantPlaceId;

    public User() { }

    public User(String uid, String username, @Nullable String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.bookedRestaurant = null;
        this.bookedRestaurantPlaceId = null;
    }

    public String getUid() { return uid; }
    public String getUsername() { return username; }
    @Nullable
    public String getUrlPicture() { return urlPicture; }
    public String getBookedRestaurant() {
        return bookedRestaurant;
    }
    public String getBookedRestaurantPlaceId() {
        return bookedRestaurantPlaceId;
    }


    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUrlPicture(@Nullable String urlPicture) { this.urlPicture = urlPicture; }
    public void setBookedRestaurant(String bookedRestaurant) {
        this.bookedRestaurant = bookedRestaurant;
    }
    public void setBookedRestaurantPlaceId(String bookedRestaurantPlaceId) {
        this.bookedRestaurantPlaceId = bookedRestaurantPlaceId;
    }
}
