package com.rudy.go4lunch.model;

import androidx.annotation.DrawableRes;

import com.rudy.go4lunch.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Workmate {

    private final String name;
    private final Restaurant mRestaurant;
    private final boolean choose;
    @DrawableRes
    private final int avatar;

    public Workmate(String name, int avatar, Restaurant restaurant, boolean choose) {
        this.name = name;
        this.avatar = avatar;
        this.mRestaurant = restaurant;
        this.choose = choose;
    }

    public static List<Workmate> DUMMY_WORKMATES = Arrays.asList(
            new Workmate("Scarlett", R.drawable.circle, Restaurant.getRestaurant()[0], true),
            new Workmate("Hugh", R.drawable.circle, Restaurant.getRestaurant()[0], true),
            new Workmate("Nana", R.drawable.circle, Restaurant.getRestaurant()[0], true),
            new Workmate("Godfrey", R.drawable.circle, Restaurant.getRestaurant()[0], false));

    public static List<Workmate> getWorkmates(){
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

    public boolean isChoose() {
        return choose;
    }
}
