package com.rudy.go4lunch;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.gson.Gson;
import com.rudy.go4lunch.model.dto.RestaurantWrapperDto;
import com.rudy.go4lunch.model.dto.RestaurantsWrapperDto;
import com.rudy.go4lunch.service.GooglePlacesRestaurantsApi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import io.reactivex.Single;

@RunWith(AndroidJUnit4.class)
public class retroMockTest {

    @Mock
    private GooglePlacesRestaurantsApi api;

    Context context = ApplicationProvider.getApplicationContext();

    @Test
    public void testGetNearbySearch() {
        String location = "123,456";
        String types = "restaurant";
        String key = "API_KEY";
        int radius = 1000;
        String keyword = "restaurant";

        RestaurantsWrapperDto mockResponse = new Gson().fromJson(loadJsonFromAssetFile(), RestaurantsWrapperDto.class);;
        Single<RestaurantsWrapperDto> mockObservable = Single.just(mockResponse);
        when(api.getNearbySearch(location, types, key, radius, keyword))
                .thenReturn(mockObservable);
        Single<RestaurantsWrapperDto> testObservable = api.getNearbySearch(location, types, key, radius, keyword);
        assertEquals(mockObservable, testObservable);
    }

    @Test
    public void testGetDetails() {
//        String placeId = "PLACE_ID";
//        String fields = "formatted_phone_number,website";
//        String key = "API_KEY";
//
//        RestaurantWrapperDto mockResponse = new RestaurantWrapperDto();
//        Single<RestaurantWrapperDto> mockObservable = Single.just(mockResponse);
//        when(api.getDetails(placeId, fields, key))
//                .thenReturn(mockObservable);
//        Single<RestaurantWrapperDto> testObservable = api.getDetails(placeId, fields, key);
//        assertEquals(mockObservable, testObservable);
    }

    private String loadJsonFromAssetFile() {
        String json = null;
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("mock_response.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            json = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
