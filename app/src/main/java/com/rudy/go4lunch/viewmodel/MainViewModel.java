package com.rudy.go4lunch.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.rudy.go4lunch.model.PredictionsDto;
import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.model.User;
import com.rudy.go4lunch.repository.PredictionRepository;
import com.rudy.go4lunch.repository.RestaurantRepository;
import com.rudy.go4lunch.repository.UserRepository;
import com.rudy.go4lunch.service.OnSearchListener;
import com.rudy.go4lunch.service.ProcessPredictionsDto;
import com.rudy.go4lunch.service.ProcessRestaurantDto;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends ViewModel {

    RestaurantRepository restaurantRepository = new RestaurantRepository();
    PredictionRepository predictionRepository = new PredictionRepository();
    private  UserRepository userRepository = new UserRepository();
    private LiveData<List<RestaurantDto>> restaurant = restaurantRepository.getNearBySearchRestaurantList();

    @SuppressLint("CheckResult")
    public void getRestaurantLocation(ProcessRestaurantDto processRestaurantDto, Location location) {
        restaurantRepository.getRestaurantLocation(location, processRestaurantDto);
    }

    public LiveData<List<RestaurantDto>> getRestaurant() {
        return restaurant;
    }

    /**
     * user view model
     */

    public LiveData<List<User>> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public void getDataBaseInstanceUser() {
        userRepository.getDataBaseInstance();
    }

    public void getPredictionLocation(ProcessPredictionsDto processPredictionsDto, Location location, String newText) {
        predictionRepository.getPredictions(location, newText, processPredictionsDto);
    }

    public LiveData<List<PredictionsDto>> getPredictionList(Location location, String newText) {
        return predictionRepository.getPredictionsList(location, newText);
    }
}

