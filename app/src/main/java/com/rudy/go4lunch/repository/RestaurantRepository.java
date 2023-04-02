package com.rudy.go4lunch.repository;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import com.google.gson.Gson;
import com.rudy.go4lunch.BuildConfig;
import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.model.dto.OpeningHoursDto;
import com.rudy.go4lunch.model.dto.RestaurantWrapperDto;
import com.rudy.go4lunch.model.dto.RestaurantsWrapperDto;
import com.rudy.go4lunch.service.GooglePlacesRestaurantsApi;
import com.rudy.go4lunch.service.ProcessRestaurantDto;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RestaurantRepository {

    private final GooglePlacesRestaurantsApi RESTAURANTS_SERVICE;
    private static final String RESTAURANT = "restaurant";
    private static final int RADIUS = 1500;
    private static final String PLACES_API_KEY = BuildConfig.PLACES_API_KEY;

    public Single<RestaurantsWrapperDto> getGooglePlacesRestaurants(Location location) {
        return RESTAURANTS_SERVICE.getNearbySearch(
                location.getLatitude() + "," + location.getLongitude(),
                RESTAURANT,
                PLACES_API_KEY,
                RADIUS,
                RESTAURANT
        ).doOnSuccess(response -> Log.d("JSON NearbySearch", new Gson().toJson(response)));
    }

    public Single<RestaurantWrapperDto> getDetails(String placeId) {
        return RESTAURANTS_SERVICE.getDetails(
                placeId,
                "formatted_phone_number,website,opening_hours,name,rating,vicinity,photos",
                PLACES_API_KEY
        ).doOnSuccess(response -> Log.d("JSON Detail", new Gson().toJson(response)));
    }

    public RestaurantRepository(GooglePlacesRestaurantsApi googlePlacesRestaurantsApi) {
        this.RESTAURANTS_SERVICE = googlePlacesRestaurantsApi;
    }

    @SuppressLint("CheckResult")
    public void getRestaurantLocation(Location location, ProcessRestaurantDto processRestaurantDto) {
        getGooglePlacesRestaurants(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(restaurantsWrapperDto -> {
                    List<RestaurantDto> restaurantDtoList = restaurantsWrapperDto.getResults();
                    for (RestaurantDto restaurantDto : restaurantDtoList) {
                        String placeId = restaurantDto.getPlaceId();
                        getDetails(placeId).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(restaurantWrapperDto -> {
                                    if (restaurantWrapperDto.getResult().getFormattedPhoneNumber() != null) {
                                        restaurantDto.setFormattedPhoneNumber(restaurantWrapperDto.getResult().getFormattedPhoneNumber());
                                    }
                                    if (restaurantWrapperDto.getResult().getWebsite() != null) {
                                        restaurantDto.setWebsite(restaurantWrapperDto.getResult().getWebsite());
                                    }
                                    if (restaurantWrapperDto.getResult().getOpeningHours() != null) {
                                        OpeningHoursDto openingHoursDto = new OpeningHoursDto(restaurantWrapperDto.getResult().getOpeningHours().isOpenNow(),
                                                restaurantWrapperDto.getResult().getOpeningHours().getWeekDay(),
                                                restaurantWrapperDto.getResult().getOpeningHours().getPeriods());
                                        restaurantDto.setOpeningHours(openingHoursDto);
                                    }
                                    if (restaurantDtoList.indexOf(restaurantDto) == restaurantDtoList.size() - 1) {
                                        processRestaurantDto.processRestaurantDto(restaurantDtoList);
                                    }
                                    Log.v("details", restaurantWrapperDto.getResult().toString());
                                }, throwable1 -> Log.v("throwable", throwable1.toString()));
                    }
                    Log.v("nearbySearch", restaurantsWrapperDto.getResults().toString());
                }, throwable -> Log.v("throwable", throwable.toString()));
    }

    @SuppressLint("CheckResult")
    public void getRestaurantOnFocus(String placeId, ProcessRestaurantDto processRestaurantDto) {
        List<RestaurantDto> restaurantDtoList = new ArrayList<>();
        getDetails(placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(restaurantWrapperDto -> {
                    if (restaurantWrapperDto.getResult() != null) {
                        restaurantWrapperDto.getResult().setPlaceId(placeId);
                        restaurantDtoList.add(restaurantWrapperDto.getResult());
                        processRestaurantDto.processRestaurantDto(restaurantDtoList);
                    }
                }, throwable -> Log.v("throwable", throwable.toString()));
    }
}
