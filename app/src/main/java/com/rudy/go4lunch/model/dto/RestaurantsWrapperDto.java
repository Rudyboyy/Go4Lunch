package com.rudy.go4lunch.model.dto;

import com.google.gson.annotations.SerializedName;
import com.rudy.go4lunch.model.RestaurantDto;

import java.io.Serializable;
import java.util.List;

public class RestaurantsWrapperDto implements Serializable {

    @SerializedName("results")
    List<RestaurantDto> Results;

    public RestaurantsWrapperDto(List<RestaurantDto> results) {
        Results = results;
    }

    public List<RestaurantDto> getResults() {
        return Results;
    }

    public void setResults(List<RestaurantDto> results) {
        Results = results;
    }
}
