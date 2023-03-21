package com.rudy.go4lunch.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CloseDto implements Serializable {

    @SerializedName("day")
    int day;

    @SerializedName("time")
    String time;

    public CloseDto(int day, String time) {
        this.day = day;
        this.time = time;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
