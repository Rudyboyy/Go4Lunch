package com.rudy.go4lunch.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LocationDto implements Serializable  {

    @SerializedName("lat")
    double latitude;

    @SerializedName("lng")
    double longitude;

    public LocationDto(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
