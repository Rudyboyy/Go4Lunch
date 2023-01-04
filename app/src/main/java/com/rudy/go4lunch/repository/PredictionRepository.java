package com.rudy.go4lunch.repository;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rudy.go4lunch.BuildConfig;
import com.rudy.go4lunch.model.PredictionsDto;
import com.rudy.go4lunch.model.dto.predictions.AutoCompleteDto;
import com.rudy.go4lunch.service.GooglePlacesRestaurantsApi;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class PredictionRepository {

    private Retrofit retrofit;

    private GooglePlacesRestaurantsApi PREDICTIONS_SERVICE;
    private static final int RADIUS = 1500;
    private static final String PLACES_API_KEY = BuildConfig.PLACES_API_KEY;

    private final MutableLiveData<List<PredictionsDto>> predictionsListLiveData = new MutableLiveData<>();

    public Single<AutoCompleteDto> getAutoComplete(Location location, String newText) {
        return PREDICTIONS_SERVICE.getAutocomplete(
                newText,
                PLACES_API_KEY,
                location.getLatitude() + "," + location.getLongitude(),
                RADIUS
        );
    }

    public PredictionRepository() {
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

        PREDICTIONS_SERVICE = retrofit.create(GooglePlacesRestaurantsApi.class);
    }

    @SuppressLint("CheckResult")
    public void getPredictions(Location location, String newText) {
        getAutoComplete(location, newText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((autoCompleteDto, throwable) -> {
                    predictionsListLiveData.setValue(autoCompleteDto.getPredictions());

                    if (throwable != null) {
                        Log.v("throwable", throwable.toString());
                    }
                });

        getAutoComplete(location, newText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<AutoCompleteDto>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // Optionnel: effectuez des actions avant que la requête ne soit lancée
                    }

                    @Override
                    public void onSuccess(AutoCompleteDto dto) {
                        // Récupérez les suggestions de l'API et affichez-les dans le RecyclerView
                        List<PredictionsDto> suggestions = dto.getPredictions();
//                        adapter.setData(suggestions); todo faire une interface
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @SuppressLint("CheckResult")
    public LiveData<List<PredictionsDto>> getPredictionsList(Location location, String newText) {
        getAutoComplete(location, newText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((autoCompleteDto, throwable) -> {
                    predictionsListLiveData.setValue(autoCompleteDto.getPredictions());

                    if (throwable != null) {
                        Log.v("throwable", throwable.toString());
                    }
                });
        return predictionsListLiveData;
    }
}
