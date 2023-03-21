package com.rudy.go4lunch.repository;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import com.google.gson.Gson;
import com.rudy.go4lunch.BuildConfig;
import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.model.dto.RestaurantWrapperDto;
import com.rudy.go4lunch.model.dto.predictions.AutoCompleteDto;
import com.rudy.go4lunch.service.GooglePlacesRestaurantsApi;
import com.rudy.go4lunch.service.ProcessDetailsRestaurant;
import com.rudy.go4lunch.service.ProcessPredictionsDto;
import com.rudy.go4lunch.service.ProcessRestaurantDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class PredictionRepository {

    private GooglePlacesRestaurantsApi PREDICTIONS_SERVICE;
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

    public PredictionRepository() {
        String GOOGLE_PLACE_URL = "https://maps.googleapis.com/maps/api/place/";

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).addInterceptor(httpLoggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(GOOGLE_PLACE_URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();

        PREDICTIONS_SERVICE = retrofit.create(GooglePlacesRestaurantsApi.class);
    }

    @SuppressLint("CheckResult")
    public void getPredictions(Location location, String newText, ProcessPredictionsDto processPredictionsDto) {
        getAutoComplete(location, newText).timeout(30, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(autoCompleteDto -> {
            processPredictionsDto.processPredictionsDto(autoCompleteDto.getPredictions());
        }, throwable -> {
            Log.v("MyProbleme", throwable.toString());
        });
    }

    @SuppressLint("CheckResult")
    public void getDetailsPrediction(ProcessDetailsRestaurant processDetailsRestaurant, ProcessRestaurantDto processRestaurantDto) {
        List<RestaurantDto> restaurantDtoListMutableLiveData = new ArrayList<>();
        for (String placeId : processDetailsRestaurant.processDetailsRestaurant()) {
            getDetails(placeId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(restaurantWrapperDto -> {
                        if (restaurantWrapperDto.getResult() != null) {
                            restaurantDtoListMutableLiveData.add(restaurantWrapperDto.getResult());
                            restaurantDtoListMutableLiveData.get(0).setWebsite(restaurantWrapperDto.getResult().getWebsite());
                            Log.d("RESTAURANT", restaurantWrapperDto.getResult().getName());
                            processRestaurantDto.processRestaurantDto(restaurantDtoListMutableLiveData);
                        }
                    }, throwable -> {
                        Log.v("MyProbleme", throwable.toString());
                    });
        }
    }
}
