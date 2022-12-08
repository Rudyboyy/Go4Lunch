package com.rudy.go4lunch.model;

import java.util.Date;

public class Booking {

    private Date date;
    private String userWhoBooked;
    private String bookedRestaurant;

    public Booking() {
    }
    
    public Booking(String user, String bookedRestaurant) {
        this.userWhoBooked = user;
        this.bookedRestaurant = bookedRestaurant;
        this.date = null;
    }

    public Date getDate() {
        return date;
    }

    public String getUser() {
        return userWhoBooked;
    }

    public String getBookedRestaurant() {
        return bookedRestaurant;
    }
}
