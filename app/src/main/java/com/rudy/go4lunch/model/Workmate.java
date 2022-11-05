package com.rudy.go4lunch.model;

import androidx.annotation.DrawableRes;

import com.rudy.go4lunch.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Workmate {

    private String name;
    private Restaurant mRestaurant;
    private boolean chose;
    @DrawableRes
    private final int avatar;
    private String id;

    public Workmate(String id, String name, int avatar, Restaurant restaurant, boolean chose) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.mRestaurant = restaurant;
        this.chose = chose;
    }

    public Workmate(String id, String name, int avatar) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.mRestaurant = null;
        this.chose = false;
    }

    public static List<Workmate> DUMMY_WORKMATES = Arrays.asList(
            new Workmate(null, "Scarlett",R.drawable.user_profile , Restaurant.DUMMY_RESTAURANTS.get(0), true),
            new Workmate(null, "Hugh", R.drawable.user_profile, Restaurant.DUMMY_RESTAURANTS.get(1), true),
            new Workmate(null, "Nana", R.drawable.user_profile, Restaurant.DUMMY_RESTAURANTS.get(2), true),
            new Workmate(null, "Godfrey", R.drawable.user_profile));

    public static List<Workmate> getWorkmates() {
        return new ArrayList<>(DUMMY_WORKMATES);
    }

    public String getName() {
        return name;
    }

    public int getAvatar() {
        return avatar;
    }

    public Restaurant getRestaurant() {
        return mRestaurant;
    }

    public boolean isChose() {
        return chose;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRestaurant(Restaurant restaurant) {
        mRestaurant = restaurant;
    }

    public void setChoice(boolean chose) {
        this.chose = chose;
    }

    public void setId(String id) {
        this.id = id;
    }
}
