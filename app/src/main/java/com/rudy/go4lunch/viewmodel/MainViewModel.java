package com.rudy.go4lunch.viewmodel;

import static com.rudy.go4lunch.ui.restaurant.RestaurantsFragment.RESTAURANT_INFO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rudy.go4lunch.manager.UserManager;
import com.rudy.go4lunch.model.PredictionsDto;
import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.model.User;
import com.rudy.go4lunch.repository.PredictionRepository;
import com.rudy.go4lunch.repository.RestaurantRepository;
import com.rudy.go4lunch.repository.UserRepository;
import com.rudy.go4lunch.ui.restaurant.DetailRestaurantActivity;

import java.util.List;

public class MainViewModel extends ViewModel {

    RestaurantRepository restaurantRepository;// = new RestaurantRepository();
    private final UserRepository userRepository = new UserRepository();
    PredictionRepository predictionRepository;// = new PredictionRepository();
    UserManager mUserManager = UserManager.getInstance();

    private final MutableLiveData<List<RestaurantDto>> restaurantListLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<RestaurantDto>> predictionsListLiveData = new MutableLiveData<>();

    public MainViewModel(RestaurantRepository restaurantRepository, PredictionRepository predictionRepository) {
        this.restaurantRepository = restaurantRepository;
        this.predictionRepository = predictionRepository;
    }

    public LiveData<List<RestaurantDto>> getRestaurantListLiveData() {
        return restaurantListLiveData;
    }

    @SuppressLint("CheckResult")
    public void getRestaurantLocation(Location location, Context context) {
//        restaurantRepository = new RestaurantRepository(context);
        restaurantRepository.getRestaurantLocation(location, restaurantListLiveData::setValue);
        //todo faire une interface pour retrofit
        //todo faire un viewModelFactory ?
    }
    @SuppressLint("CheckResult")
    public void getRestaurantOnFocus(String placeId, Context context) {
//        restaurantRepository = new RestaurantRepository(context);
        restaurantRepository.getRestaurantOnFocus(placeId, restaurantDtoList -> mUserManager.getUserData().addOnSuccessListener(user -> {
            if (restaurantDtoList != null) {
                RestaurantDto restaurantOnFocus = restaurantDtoList.get(0);
                Intent detailRestaurantActivityIntent = new Intent(context, DetailRestaurantActivity.class);
                detailRestaurantActivityIntent.putExtra(RESTAURANT_INFO, restaurantOnFocus);
                detailRestaurantActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(detailRestaurantActivityIntent);
            }
        }));
    }

    public LiveData<List<RestaurantDto>> getPredictionLocation(Location location, String newText) {
        predictionRepository.getPredictions(location, newText, this::getPredictions);
        return predictionsListLiveData;
    }

    public void getPredictions(List<PredictionsDto> predictionsDtoList) {
        predictionRepository.getDetailsPrediction(predictionsDtoList, predictionsListLiveData::setValue);
    }

    public LiveData<List<User>> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public void getDataBaseInstanceUser() {
        userRepository.getDataBaseInstance();
    }
}

