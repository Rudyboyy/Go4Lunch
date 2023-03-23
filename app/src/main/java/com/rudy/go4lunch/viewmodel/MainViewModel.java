package com.rudy.go4lunch.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.rudy.go4lunch.model.User;
import com.rudy.go4lunch.repository.PredictionRepository;
import com.rudy.go4lunch.repository.RestaurantRepository;
import com.rudy.go4lunch.repository.UserRepository;
import com.rudy.go4lunch.service.ProcessDetailsRestaurant;
import com.rudy.go4lunch.service.ProcessPredictionsDto;
import com.rudy.go4lunch.service.ProcessRestaurantDto;

import java.util.List;

public class MainViewModel extends ViewModel {

    RestaurantRepository restaurantRepository;// = new RestaurantRepository();
    private UserRepository userRepository = new UserRepository();
    //        private LiveData<List<RestaurantDto>> restaurant = restaurantRepository.getNearBySearchRestaurantList();
    PredictionRepository predictionRepository = new PredictionRepository();

    @SuppressLint("CheckResult")
    public void getRestaurantLocation(ProcessRestaurantDto processRestaurantDto, Location location, Context context) {
        restaurantRepository = new RestaurantRepository(context);
        restaurantRepository.getRestaurantLocation(location, processRestaurantDto);
    }
    @SuppressLint("CheckResult")
    public void getRestaurantOnFocus(String placeId, ProcessRestaurantDto processRestaurantDto, Context context) {
        restaurantRepository = new RestaurantRepository(context);
        restaurantRepository.getRestaurantOnFocus(placeId, processRestaurantDto);
    }


//    public LiveData<List<RestaurantDto>> getRestaurant() {
//        return restaurant;
//    }

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

    public void getPrediction(ProcessDetailsRestaurant processDetailsRestaurant, ProcessRestaurantDto processRestaurantDto) {
        predictionRepository.getDetailsPrediction(processDetailsRestaurant, processRestaurantDto);
    }

//    public LiveData<List<PredictionsDto>> getPredictionList(Location location, String newText) {
//        return predictionRepository.getPredictionsList(location, newText);
//    }
}

