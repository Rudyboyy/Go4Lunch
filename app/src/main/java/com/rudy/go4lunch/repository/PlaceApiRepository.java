//package com.rudy.go4lunch.repository;
//
//import static android.content.ContentValues.TAG;
//
//import android.os.Handler;
//import android.os.Looper;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//
//import com.rudy.go4lunch.BuildConfig;
//import com.rudy.go4lunch.model.NearbySearch;
//import com.rudy.go4lunch.service.RetrofitNearbySearchApi;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.inject.Inject;
//import javax.xml.transform.Result;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class PlaceApiRepository {
//
//    private static final String PLACES_API_KEY = BuildConfig.PLACES_API_KEY;
//    private static final String RESTAURANT = "restaurant";
//    private static final String RANK_BY = "distance";
//    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";
//    private static final int MAXPRICE = 2;
//    private static final int HANDLING_TIME = 2000;
//    private final Handler handler = new Handler(Looper.getMainLooper());
//    private final MutableLiveData<List<NearbySearch.ResultsItem>> restaurantListLiveData = new MutableLiveData<>();
//    private final List<NearbySearch.ResultsItem> restaurantList = new ArrayList<>();
//
//    final Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build();
//
//    final RetrofitNearbySearchApi retrofitNearBySearchAPI = retrofit.create(RetrofitNearbySearchApi.class);
//
//    /**
//     * NearBySearch
//     */
//
//    public LiveData<List<NearbySearch.ResultsItem>> getNearBySearchRestaurantList() {
//        return restaurantListLiveData;
//    }
//
//    //Getting NearBySearch results as a list using the radius parameter and managing the recall if there is a page token
//
//    public void fetchNearBySearchPlaces(String location, int radius) {
//
//        Call<NearbySearch.PlaceNearBy> call = retrofitNearBySearchAPI.getNearByPlacesRadiusMethod(location, radius, MAXPRICE, RESTAURANT, PLACES_API_KEY, null);
//        call.enqueue(new Callback<NearbySearch.PlaceNearBy>() {
//            @Override
//            public void onResponse(@NonNull Call<NearbySearch.PlaceNearBy> call, @NonNull Response<NearbySearch.PlaceNearBy> response) {
//
//                if (!response.isSuccessful()) {
//                    Log.w(TAG, "onResponse: no response", null);
//                    return;
//                }
//
//                assert response.body() != null;
//                populateList(response.body().getResults());
//
//                if (response.body().getNextPageToken() != null) {
//                    handler.postDelayed(() -> getNextResultsRadiusMethod(location, radius, response.body().getNextPageToken()), HANDLING_TIME);
//                } else {
//                    noRestaurantInRadius(location);
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<NearbySearch.PlaceNearBy> call, Throwable t) {
//                Log.e(TAG, "onFailure: API call failed", t);
//            }
//        });
//    }
//
//    //PageToken recall if it is found
//
//    public void getNextResultsRadiusMethod(String location, int radius, String pageToken) {
//
//        Call<NearbySearch.PlaceNearBy> call = retrofitNearBySearchAPI.getNearByPlacesRadiusMethod(location, radius, MAXPRICE, RESTAURANT, PLACES_API_KEY, pageToken);
//        call.enqueue(new Callback<NearbySearch.PlaceNearBy>() {
//
//            @Override
//            public void onResponse(@NonNull Call<NearbySearch.PlaceNearBy> call, @NonNull Response<NearbySearch.PlaceNearBy> response) {
//
//                if (!response.isSuccessful()) {
//                    Log.w(TAG, "onResponse: no response", null);
//                    return;
//                }
//
//                assert response.body() != null;
//                populateList(response.body().getResults());
//
//                if (response.body().getNextPageToken() != null) {
//
//                    Call<NearbySearch.PlaceNearBy> call2 = retrofitNearBySearchAPI.getNearByPlacesRadiusMethod(location, radius, MAXPRICE, RESTAURANT, PLACES_API_KEY, response.body().getNextPageToken());
//                    call2.enqueue(new Callback<NearbySearch.PlaceNearBy>() {
//
//                        @Override
//                        public void onResponse(@NonNull Call<NearbySearch.PlaceNearBy> call, @NonNull Response<NearbySearch.PlaceNearBy> response) {
//                            if (!response.isSuccessful()) {
//                                Log.w(TAG, "onResponse: no response", null);
//                                return;
//                            }
//
//                            assert response.body() != null;
//                            populateList(response.body().getResults());
//                        }
//
//
//                        @Override
//                        public void onFailure(@NonNull Call<NearbySearch.PlaceNearBy> call, @NonNull Throwable t) {
//                            Log.e(TAG, "onFailure: API call failed", t);
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<NearbySearch.PlaceNearBy> call, @NonNull Throwable t) {
//                Log.e(TAG, "onFailure: API call failed", t);
//            }
//        });
//    }
//
//    //API called when API with Radius method does not return anything
//
//    private void noRestaurantInRadius(String location) {
//        Call<NearbySearch.PlaceNearBy> call3 = retrofitNearBySearchAPI.getNearByPlacesRankByMethod(location, RANK_BY, MAXPRICE, RESTAURANT, PLACES_API_KEY, null);
//
//        call3.enqueue(new Callback<NearbySearch.PlaceNearBy>() {
//            @Override
//            public void onResponse(@NonNull Call<NearbySearch.PlaceNearBy> call, @NonNull Response<NearbySearch.PlaceNearBy> response) {
//
//                if (!response.isSuccessful()) {
//                    Log.w(TAG, "onResponse: no response", null);
//                    return;
//                }
//
//                assert response.body() != null;
//                populateList(response.body().getResults());
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<NearbySearch.PlaceNearBy> call, @NonNull Throwable t) {
//                Log.e(TAG, "onFailure: API call failed", t);
//            }
//        });
//    }
//
//    //populate the list to avoid double results
//
//    private void populateList(List<NearbySearch.ResultsItem> list) {
//        for (NearbySearch.ResultsItem newItem : list) {
//            boolean isNotInList = true;
//
//            for (NearbySearch.ResultsItem oldItem : restaurantList) {
//                if (oldItem.getName().equalsIgnoreCase(newItem.getName())) {
//                    isNotInList = false;
//                    break;
//                }
//            }
//            if (isNotInList) {
//                restaurantList.add(newItem);
//            }
//        }
//        restaurantListLiveData.setValue(restaurantList);
//    }
//}
