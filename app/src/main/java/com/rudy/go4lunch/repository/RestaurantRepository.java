package com.rudy.go4lunch.repository;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rudy.go4lunch.BuildConfig;
import com.rudy.go4lunch.model.PredictionsDto;
import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.model.dto.RestaurantWrapperDto;
import com.rudy.go4lunch.model.dto.RestaurantsWrapperDto;
import com.rudy.go4lunch.model.dto.predictions.AutoCompleteDto;
import com.rudy.go4lunch.service.GooglePlacesRestaurantsApi;
import com.rudy.go4lunch.service.ProcessRestaurantDto;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
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
                RADIUS,
                RESTAURANT

        );
    }

    public Single<RestaurantWrapperDto> getDetails(String placeId) {
        return RESTAURANTS_SERVICE.getDetails(
                placeId,
                "formatted_phone_number,website",
                PLACES_API_KEY
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

    public Single<AutoCompleteDto> getAutoComplete(Location location, String newText) {//todo test
        return RESTAURANTS_SERVICE.getAutocomplete(
                newText,
                PLACES_API_KEY,
                location.getLatitude() + "," + location.getLongitude(),
                RADIUS,
                RESTAURANT
        );
    }

    @SuppressLint("CheckResult")
    public void getRestaurantLocation(Location location, ProcessRestaurantDto processRestaurantDto) {
        getGooglePlacesRestaurants(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((restaurantDtos, throwable) -> {
                    for (RestaurantDto restaurantDto : restaurantDtos.getResults()) {
                        String placeId = restaurantDto.getPlaceId();
                        getDetails(placeId).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(restaurantWrapperDto -> {
                                    restaurantDto.setFormattedPhoneNumber(restaurantWrapperDto.getResult().getFormattedPhoneNumber());
                                    restaurantDto.setWebsite(restaurantWrapperDto.getResult().getWebsite());
                                    processRestaurantDto.processRestaurantDto(restaurantDtos.getResults());
                                    Log.v("details", restaurantWrapperDto.getResult().toString());
//                                    if (newText != null) {
//                                        getAutoComplete(location, newText)//todo test
//                                                .subscribeOn(Schedulers.io())
//                                                .observeOn(AndroidSchedulers.mainThread())
//                                                .subscribe((autoCompleteDto) -> {
//                                                    Log.v("autoComplete", autoCompleteDto.getPredictions().toString());
//                                                    List<RestaurantDto> predictionsRestaurants = new ArrayList<>();
//                                                    for (PredictionsDto predictionsDto : autoCompleteDto.getPredictions()) {
//                                                        String suggestion = predictionsDto.getStructuredFormatting().getMainText().toLowerCase().trim();
//                                                        String restaurantName = restaurantDto.getName().toLowerCase().trim();
//                                                        String restaurantString = restaurantDto.toString().toLowerCase().trim();
//                                                        if (restaurantName.contains(suggestion)) {
//                                                            predictionsRestaurants.add(restaurantDto);
//                                                            processRestaurantDto.processRestaurantDto(predictionsRestaurants);
//                                                        }
////                                                        List<RestaurantDto> filteredRestaurants = new ArrayList<>();
////                                                        for (RestaurantDto restaurant : restaurantDtos.getResults()) {
////                                                            if (restaurant.getName().contains(newText)) {
////                                                                filteredRestaurants.add(restaurant);
////                                                            }
////                                                        }
////                                                        processRestaurantDto.processRestaurantDto(filteredRestaurants);
//                                                    }
//                                                    if (throwable != null) {
//                                                        Log.v("throwable", throwable.toString());
//                                                    }
//                                                });
//                                    }
                                });
                    }
                    if (throwable != null) {
                        Log.v("throwable", throwable.toString());
                    }
                });
    }

    @SuppressLint("CheckResult")
    public LiveData<List<RestaurantDto>> getRestaurantList(Location location) {
        getGooglePlacesRestaurants(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((restaurantDtos, throwable) -> {
                    restaurantListLiveData.setValue(restaurantDtos.getResults());
                    if (throwable != null) {
                        Log.v("throwable", throwable.toString());
                    }
                });
        return restaurantListLiveData;
    }

    public LiveData<List<RestaurantDto>> getNearBySearchRestaurantList() {
        return restaurantListLiveData;
    }
}
