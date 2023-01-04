package com.rudy.go4lunch.service;

import com.rudy.go4lunch.model.dto.RestaurantWrapperDto;
import com.rudy.go4lunch.model.dto.RestaurantsWrapperDto;
import com.rudy.go4lunch.model.dto.predictions.AutoCompleteDto;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlacesRestaurantsApi {

    @GET("nearbysearch/json")
    Single<RestaurantsWrapperDto> getNearbySearch(@Query(value = "location", encoded = true) String location,
                                                  @Query("type") String types,
                                                  @Query("key") String key,
                                                  @Query("radius") int radius,
                                                  @Query("keyword") String keyword);

    @GET("details/json")
    Single<RestaurantWrapperDto> getDetails(@Query("place_id") String placeId,
                                            @Query(value = "fields", encoded = true) String fields,
                                            @Query("key") String key);

    @GET("autocomplete/json")
    Single<AutoCompleteDto> getAutocomplete(@Query("input") String input,
                                            @Query("key") String key,
                                            @Query("location") String location,
                                            @Query("radius") int radius);
//                                                  @Query("types") String types);
}
