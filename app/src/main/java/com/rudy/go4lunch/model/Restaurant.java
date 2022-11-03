package com.rudy.go4lunch.model;

import androidx.annotation.DrawableRes;

import com.rudy.go4lunch.R;

import java.io.Serializable;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Restaurant implements Serializable {

    String name;
    String foodStyle;
    String address;
    long phoneNumber;
    List<Workmate> attendees;
    int starRate;
    boolean isFavorite;
    int distance;
    URL website;
    LocalTime openingTime;
    LocalTime closingTime;
    @DrawableRes
    private final int picture;

    public Restaurant(String name, String foodStyle, String address, int phoneNumber , int starRate, int picture) {
        this.name = name;
        this.foodStyle = foodStyle;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.starRate = starRate;
        this.attendees = attendees;
        this.picture = picture;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    //todo add workmate
    public static List<Restaurant> DUMMY_RESTAURANTS = Arrays.asList(
            new Restaurant("Les Toqués", "French", "1 Mealstreet New York", 875, 2, R.drawable.restaurant),
            new Restaurant("Panda Food", "chinese", "13 Tigerstreet New York", 514, 1, R.drawable.restaurant),
            new Restaurant("Burger Mania", "American", "4 Mainstreet New York", 872, 3, R.drawable.restaurant),
            new Restaurant("Casa Nostra", "italian", "78 Napolistreet", 789, 2, R.drawable.restaurant),
            new Restaurant("Le Zinc", "French", "12 rue Faugbourd poissonière", 615, 1, R.drawable.restaurant),
            new Restaurant("Le Seoul", "Korean", "18 rue du Paradis", 675, 0, R.drawable.restaurant),
            new Restaurant("Tokyomaki", "japanese", "24 rue des Petites Ecoles", 789, 2, R.drawable.restaurant));

    public static List<Restaurant> getRestaurants() {
        return new ArrayList<>(DUMMY_RESTAURANTS);
    }

    public String getName() {
        return name;
    }

    public String getFoodStyle() {
        return foodStyle;
    }

    public String getAddress() {
        return address;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public List<Workmate> getAttendees() {
        return attendees;
    }

    public int getWorkmatesNumber() {
        return getAttendees().size();
    }

    public int getStarRate() {
        return starRate;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public int getDistance() {
        return distance;
    }
}
