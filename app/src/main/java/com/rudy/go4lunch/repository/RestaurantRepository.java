package com.rudy.go4lunch.repository;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rudy.go4lunch.BuildConfig;
import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.model.dto.RestaurantsWrapperDto;
import com.rudy.go4lunch.service.GooglePlacesRestaurantsApi;

import java.util.List;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantRepository {

    private Retrofit retrofit;

    private GooglePlacesRestaurantsApi RESTAURANTS_SERVICE;
    private static final String RESTAURANT = "restaurant";
    private static final int RADIUS = 1500;
    private static final String PLACES_API_KEY = BuildConfig.PLACES_API_KEY;
    private final MutableLiveData<List<RestaurantDto>> restaurantListLiveData = new MutableLiveData<>();

    public Single<RestaurantsWrapperDto> getGooglePlacesRestaurants(Location location) {
        return RESTAURANTS_SERVICE.getNearbySearch(
                location.getLatitude() + "," + location.getLongitude(),
                RESTAURANT,
                PLACES_API_KEY,
                RADIUS
        );
    }

    public LiveData<List<RestaurantDto>> getRestaurants() {
        return restaurantListLiveData;
    }

    public RestaurantRepository() {
        String GOOGLE_PLACE_URL = "https://maps.googleapis.com/maps/api/place/";

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(httpLoggingInterceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(GOOGLE_PLACE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        RESTAURANTS_SERVICE = retrofit.create(GooglePlacesRestaurantsApi.class);
    }
}
