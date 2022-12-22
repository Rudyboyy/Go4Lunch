package com.rudy.go4lunch.model.dto;

import com.google.gson.annotations.SerializedName;
import com.rudy.go4lunch.model.RestaurantDto;

public class RestaurantWrapperDto {

    @SerializedName("result")
    RestaurantDto Result;

    public RestaurantWrapperDto(RestaurantDto result) {
        Result = result;
    }

    public RestaurantDto getResult() {
        return Result;
    }

    public void setResults(RestaurantDto result) {
        Result = result;
    }
}
