package com.rudy.go4lunch.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.rudy.go4lunch.BuildConfig;
import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.model.dto.OpeningHoursDto;
import com.rudy.go4lunch.model.dto.RestaurantWrapperDto;
import com.rudy.go4lunch.model.dto.RestaurantsWrapperDto;
import com.rudy.go4lunch.service.GooglePlacesRestaurantsApi;
import com.rudy.go4lunch.service.GooglePlacesRestaurantsApiMock;
import com.rudy.go4lunch.service.ProcessDetailsRestaurant;
import com.rudy.go4lunch.service.ProcessRestaurantDto;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class RestaurantRepository {

    private Retrofit retrofit;

    private final GooglePlacesRestaurantsApi RESTAURANTS_SERVICE;
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
        ).doOnSuccess(response -> Log.d("JSON NearbySearch", new Gson().toJson(response)));
    }

    public Single<RestaurantWrapperDto> getDetails(String placeId) {
        return RESTAURANTS_SERVICE.getDetails(
                placeId,
                "formatted_phone_number,website,opening_hours,name,rating,vicinity,photos",
                PLACES_API_KEY
        ).doOnSuccess(response -> Log.d("JSON Detail", new Gson().toJson(response)));
    }

    public RestaurantRepository(Context context) {
//        String GOOGLE_PLACE_URL = "https://maps.googleapis.com/maps/api/place/";
//
//        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
//        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient okHttpClient = new OkHttpClient
//                .Builder()
//                .addInterceptor(httpLoggingInterceptor).build();
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl(GOOGLE_PLACE_URL)
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build();
//
//        RESTAURANTS_SERVICE = retrofit.create(GooglePlacesRestaurantsApi.class);
        RESTAURANTS_SERVICE = new GooglePlacesRestaurantsApiMock(context);
    }

    @SuppressLint("CheckResult")
    public void getRestaurantLocation(Location location, ProcessRestaurantDto processRestaurantDto) {
        getGooglePlacesRestaurants(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((restaurantDtos, throwable) -> {
                    List<RestaurantDto> restaurantDtoList = restaurantDtos.getResults();
                    for (RestaurantDto restaurantDto : restaurantDtoList) {
                        String placeId = restaurantDto.getPlaceId();
                        getDetails(placeId).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(restaurantWrapperDto -> {
                                    if (restaurantWrapperDto.getResult().getFormattedPhoneNumber() != null) {
                                        restaurantDto.setFormattedPhoneNumber(restaurantWrapperDto.getResult().getFormattedPhoneNumber());
                                    }
                                    if (restaurantWrapperDto.getResult().getWebsite() != null) {
                                        restaurantDto.setWebsite(restaurantWrapperDto.getResult().getWebsite());
                                    }
                                    if (restaurantWrapperDto.getResult().getOpeningHours().getPeriods() != null) {
                                        OpeningHoursDto openingHoursDto = new OpeningHoursDto(restaurantWrapperDto.getResult().getOpeningHours().isOpenNow(),
                                                restaurantWrapperDto.getResult().getOpeningHours().getWeekDay(),
                                                restaurantWrapperDto.getResult().getOpeningHours().getPeriods());
                                        restaurantDto.setOpeningHours(openingHoursDto);
                                    }
                                    if (restaurantDtoList.indexOf(restaurantDto) == restaurantDtoList.size() - 1) {
                                        processRestaurantDto.processRestaurantDto(restaurantDtoList);
                                    }
                                    Log.v("details", restaurantWrapperDto.getResult().toString());
                                });
                    }
                    Log.v("nearbySearch", restaurantDtos.getResults().toString());
                });
    }

    @SuppressLint("CheckResult")
    public void getRestaurantOnFocus(String placeId, ProcessRestaurantDto processRestaurantDto) {
        List<RestaurantDto> restaurantDtoList = new ArrayList<>();
            getDetails(placeId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(restaurantWrapperDto -> {
                        if (restaurantWrapperDto.getResult() != null) {
                            restaurantWrapperDto.getResult().setPlaceId(placeId);
                            restaurantDtoList.add(restaurantWrapperDto.getResult());
                            processRestaurantDto.processRestaurantDto(restaurantDtoList);
                        }
                    }, throwable -> {
                        Log.v("throwable", throwable.toString());
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
