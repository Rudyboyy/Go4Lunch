package com.rudy.go4lunch.viewmodel;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.rudy.go4lunch.repository.RestaurantRepository;
import com.rudy.go4lunch.service.ProcessRestaurantDto;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends ViewModel {

    RestaurantRepository restaurantRepository;

    public MainViewModel() {

    }

    @SuppressLint("CheckResult")
    public void getRestaurantLocation(ProcessRestaurantDto processRestaurantDto, Location location) {
        restaurantRepository = new RestaurantRepository();
        restaurantRepository.getGooglePlacesRestaurants(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((restaurantDtos, throwable) -> {
                    processRestaurantDto.processRestaurantDto(restaurantDtos.getResults());
                    if (throwable != null) {
                        Log.v("throwable", throwable.toString());
                    }
                });
    }
}

