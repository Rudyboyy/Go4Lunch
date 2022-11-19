package com.rudy.go4lunch.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitRepository {

//    private final GoogleMapApi mGoogleMapAPI;

//    public RetrofitRepository(GoogleMapApi googleMapAPI) {
//        this.mGoogleMapAPI = googleMapAPI;
//    }
//
//    public LiveData<PlaceResults> getPlaceResultsLiveData(String location, int radius, String type, String apiKey) {
//        MutableLiveData <PlaceResults> PlaceResultsMutableLiveData = new MutableLiveData<>();
//        Call<PlaceResults> placeResultsCall = mGoogleMapAPI.getNearby(location, radius, type, apiKey);
//        placeResultsCall.enqueue(new Callback<PlaceResults>() {
//            @Override
//            public void onResponse(@NonNull Call<PlaceResults> call, @NonNull Response<PlaceResults> response) {
//                PlaceResultsMutableLiveData.setValue(response.body());
//            }
//            @Override
//            public void onFailure(@NonNull Call<PlaceResults> call, @NonNull Throwable t) {
//                PlaceResultsMutableLiveData.setValue(null);
//            }
//        });
//        return PlaceResultsMutableLiveData;
//    }
}
