package com.rudy.go4lunch.model;

import androidx.annotation.Nullable;

import java.util.List;

public class User {

    private String uid;
    private String username;
    @Nullable
    private String urlPicture;
    private String bookedRestaurant;
    private String bookedRestaurantPlaceId;
    private List<String> favoriteRestaurants;

    public User() { }

    public User(String uid, String username, @Nullable String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.bookedRestaurant = null;
        this.bookedRestaurantPlaceId = null;
        this.favoriteRestaurants = null;
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
    public List<String> getFavoriteRestaurants() {
        return favoriteRestaurants;
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
    public void setFavoriteRestaurants(List<String> favoriteRestaurants) {
        this.favoriteRestaurants = favoriteRestaurants;
    }
}
