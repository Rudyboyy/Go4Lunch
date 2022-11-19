package com.rudy.go4lunch.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rudy.go4lunch.model.Restaurant;
import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.model.User;
import com.rudy.go4lunch.repository.UserRepository;

import java.util.List;

public class MainViewModel extends ViewModel {

    UserRepository mUserRepository;
    MutableLiveData<List<User>> userMutableLiveData;
    MutableLiveData<List<RestaurantDto>> restaurantMutableLiveData;

    public MainViewModel() {
        mUserRepository = new UserRepository();
        restaurantMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<RestaurantDto>> getRestaurantMutableLiveData() {
        return restaurantMutableLiveData;
    }

    public MutableLiveData<List<User>> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void fetchUser() {}

    public void fetchRestaurant() {}
}

