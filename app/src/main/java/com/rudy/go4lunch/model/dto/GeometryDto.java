package com.rudy.go4lunch.model.dto;

import com.google.gson.annotations.SerializedName;

public class GeometryDto {

    @SerializedName("location")
    private LocationDto mLocationDto;

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
