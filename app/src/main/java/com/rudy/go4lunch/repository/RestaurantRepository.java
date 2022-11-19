package com.rudy.go4lunch.repository;

import com.rudy.go4lunch.BuildConfig;
import com.rudy.go4lunch.model.dto.RestaurantsWrapperDto;
import com.rudy.go4lunch.service.GooglePlacesRestaurantsApi;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantRepository {

    private Retrofit retrofit;

    private GooglePlacesRestaurantsApi RESTAURANTS_SERVICE;

    public Single<RestaurantsWrapperDto> getGooglePlacesRestaurants() {
        return RESTAURANTS_SERVICE.getNearbySearch(
                "47.899100%2C1.909360",
                "restaurant",
                BuildConfig.PLACES_API_KEY,
                1500
        );
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
