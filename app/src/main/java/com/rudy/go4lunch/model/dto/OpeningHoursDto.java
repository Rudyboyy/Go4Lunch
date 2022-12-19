package com.rudy.go4lunch.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OpeningHoursDto implements Serializable  {

    @SerializedName("open_now")
    boolean openNow;

    public OpeningHoursDto(boolean openNow) {
        this.openNow = openNow;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }
}
