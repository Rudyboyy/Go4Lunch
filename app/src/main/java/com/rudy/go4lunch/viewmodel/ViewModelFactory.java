package com.rudy.go4lunch.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.rudy.go4lunch.repository.PredictionRepository;
import com.rudy.go4lunch.repository.RestaurantRepository;
import com.rudy.go4lunch.service.RetrofitService;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory factory;
    private final RestaurantRepository mRestaurantRepository;
    private final PredictionRepository mPredictionRepository;

    public static ViewModelFactory getInstance(Context context) {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    factory = new ViewModelFactory(context);
                }
            }
        }
        return factory;
    }

    private ViewModelFactory(Context context) {
        this.mRestaurantRepository = new RestaurantRepository(
                RetrofitService.getGooglePlacesRestaurantsApi()
                // Use RetrofitService.getGooglePlacesRestaurantsApiMock(context) if you do not want to call api
        );
        this.mPredictionRepository = new PredictionRepository(
                RetrofitService.getGooglePlacesRestaurantsApi()
        );
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(mRestaurantRepository, mPredictionRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}