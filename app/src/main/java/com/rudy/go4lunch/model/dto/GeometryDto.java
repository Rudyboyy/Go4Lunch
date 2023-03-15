package com.rudy.go4lunch.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GeometryDto implements Serializable {

    @SerializedName("location")
    public LocationDto mLocationDto;

    public LocationDto getLocationDto() {
        return mLocationDto;
    }

    public void setLocationDto(LocationDto locationDto) {
        mLocationDto = locationDto;
    }

    public GeometryDto(LocationDto locationDto) {
        mLocationDto = locationDto;
    }
}
