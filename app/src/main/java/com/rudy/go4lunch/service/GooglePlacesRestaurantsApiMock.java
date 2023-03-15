package com.rudy.go4lunch.service;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.rudy.go4lunch.model.dto.RestaurantWrapperDto;
import com.rudy.go4lunch.model.dto.RestaurantsWrapperDto;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.reactivex.Single;

public class GooglePlacesRestaurantsApiMock implements GooglePlacesRestaurantsApi {
    private final Context context;

    public GooglePlacesRestaurantsApiMock(Context context) {
        this.context = context;
    }

    @Override
    public Single<RestaurantsWrapperDto> getNearbySearch(String location, String types, String key, int radius, String keyword) {
        return Single.fromCallable(() -> {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("mock_response.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            inputStream.close();
            Gson gson = new Gson();
            return gson.fromJson(stringBuilder.toString(), RestaurantsWrapperDto.class);
        });
    }

    @Override
    public Single<RestaurantWrapperDto> getDetails(String placeId, String fields, String key) {
        return Single.fromCallable(() -> {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("mock_response_detail.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            inputStream.close();
            Gson gson = new Gson();
            return gson.fromJson(stringBuilder.toString(), RestaurantWrapperDto.class);
        });
    }
}
