package com.rudy.go4lunch.service;

import com.rudy.go4lunch.model.dto.RestaurantsWrapperDto;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlacesRestaurantsApi {

    @GET("nearbysearch/json")
    Single<RestaurantsWrapperDto> getNearbySearch(@Query(value = "location", encoded = true) String location,
                                                  @Query("type") String types,
                                                  @Query("key") String key,
                                                  @Query("radius") int radius);
}
