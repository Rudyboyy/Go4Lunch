package com.rudy.go4lunch.service;

import android.content.Context;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private static final HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
    private static final String GOOGLE_PLACE_URL = "https://maps.googleapis.com/maps/api/place/";
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(GOOGLE_PLACE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    public static GooglePlacesRestaurantsApiMock getGooglePlacesRestaurantsApiMock(Context context) {
        return new GooglePlacesRestaurantsApiMock(context);
    }

    public static GooglePlacesRestaurantsApi getGooglePlacesRestaurantsApi() {
        return retrofit.create(GooglePlacesRestaurantsApi.class);
    }
}
