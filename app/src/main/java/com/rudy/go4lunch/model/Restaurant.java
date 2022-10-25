package com.rudy.go4lunch.model;

import com.rudy.go4lunch.R;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Restaurant {

    String name;
    String foodStyle;
    String address;
    int phoneNumber;
    Workmate mWorkmates;
    int star;

    LocalTime openingTime;
    LocalTime closingTime;

    public Restaurant(String name, String foodStyle, String address, int phoneNumber) {//, Workmates workmates, int star, LocalTime openingTime, LocalTime closingTime) {
        this.name = name;
        this.foodStyle = foodStyle;
        this.address = address;
        this.phoneNumber = phoneNumber;
//        this.mWorkmates = workmates;
//        this.star = star;
//        this.openingTime = openingTime;
//        this.closingTime = closingTime;
    }

    public static List<Restaurant> DUMMY_RESTAURANTS = Arrays.asList(
            new Restaurant("Les Toqués", "French", "1 Mealstreet New York", 8),
            new Restaurant("Panda Food", "chinese","13 Tigerstreet New York",514),
            new Restaurant("Burger Mania", "American", "4 Mainstreet New York", 87),
            new Restaurant("Casa Nostra", "italian", "78 Napolistreet", 789));

    public static List<Restaurant> getRestaurants(){
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

    public int getStar() {
        return star;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }
}
