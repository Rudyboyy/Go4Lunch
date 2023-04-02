package com.rudy.go4lunch.repository;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import com.google.gson.Gson;
import com.rudy.go4lunch.BuildConfig;
import com.rudy.go4lunch.model.PredictionsDto;
import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.model.dto.RestaurantWrapperDto;
import com.rudy.go4lunch.model.dto.predictions.AutoCompleteDto;
import com.rudy.go4lunch.service.GooglePlacesRestaurantsApi;
import com.rudy.go4lunch.service.ProcessPredictionsDto;
import com.rudy.go4lunch.service.ProcessRestaurantDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PredictionRepository {

    private final GooglePlacesRestaurantsApi PREDICTIONS_SERVICE;
    private static final int RADIUS = 1500;
    private static final String PLACES_API_KEY = BuildConfig.PLACES_API_KEY;
    private final String country = Locale.getDefault().getCountry();
    private final String COMPONENTS = "country:" + country;

    public Single<AutoCompleteDto> getAutoComplete(Location location, String newText) {
        return PREDICTIONS_SERVICE.getAutocomplete(newText,
                PLACES_API_KEY,
                location.getLatitude() + "," + location.getLongitude(),
                RADIUS,
                "restaurant",
                COMPONENTS);
    }

    public Single<RestaurantWrapperDto> getDetails(String placeId) {
        return PREDICTIONS_SERVICE.getDetails(placeId,
                        "geometry,opening_hours,name,rating,vicinity,place_id,photos,website,formatted_phone_number",
                        PLACES_API_KEY)
                .doOnSuccess(response -> Log.d("JSON Detail", new Gson().toJson(response)));
    }

    public PredictionRepository(GooglePlacesRestaurantsApi googlePlacesRestaurantsApi) {
        this.PREDICTIONS_SERVICE = googlePlacesRestaurantsApi;
    }

    @SuppressLint("CheckResult")
    public void getPredictions(Location location, String newText, ProcessPredictionsDto processPredictionsDto) {
        getAutoComplete(location, newText).timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(autoCompleteDto -> processPredictionsDto.processPredictionsDto(autoCompleteDto.getPredictions()), throwable -> Log.v("throwable", throwable.toString()));
    }

    @SuppressLint("CheckResult")
    public void getDetailsPrediction(List<PredictionsDto> predictionsDtoList, ProcessRestaurantDto processRestaurantDto) {
        List<RestaurantDto> restaurantDtoList = new ArrayList<>();
        for (PredictionsDto predictionsDto : predictionsDtoList) {
            getDetails(predictionsDto.getPlaceId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(restaurantWrapperDto -> {
                        if (restaurantWrapperDto.getResult() != null) {
                            restaurantDtoList.add(restaurantWrapperDto.getResult());
                            if (restaurantDtoList.indexOf(restaurantWrapperDto.getResult()) == restaurantDtoList.size() - 1) {
                                processRestaurantDto.processRestaurantDto(restaurantDtoList);
                            }
                        }
                    }, throwable -> Log.v("throwable", throwable.toString()));
        }
    }
}
