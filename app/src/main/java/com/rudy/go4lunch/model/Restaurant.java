package com.rudy.go4lunch.model;

import java.time.LocalTime;

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

    public static Restaurant[] getRestaurant() {
        return new Restaurant[]{new Restaurant("Les Toqués", "French", "1 Mealstreet New York", 8)
        };
    }

    public String getName() {
        return name;
    }

    public String getFoodStyle() {
        return foodStyle;
    }
}
