package com.rudy.go4lunch;

import android.gesture.Prediction;

import com.google.android.libraries.places.api.model.Place;
import com.rudy.go4lunch.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public interface FakeData {
    Callback<Place[]> fakePlaceCallback =
            new Callback<Place[]>() {
                @Override
                public void onResponse(Call<Place[]> call, Response<Place[]> response) {

                }

                @Override
                public void onFailure(Call<Place[]> call, Throwable t) {

                }
            };

    Callback<Place> fakeSinglePlaceCallback =
            new Callback<Place>() {
                @Override
                public void onResponse(Call<Place> call, Response<Place> response) {

                }

                @Override
                public void onFailure(Call<Place> call, Throwable t) {

                }
            };

    Callback<User> fakeSingleUserCallback =
            new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            };

    Callback<User[]> fakeUserCallback =
            new Callback<User[]>() {
                @Override
                public void onResponse(Call<User[]> call, Response<User[]> response) {

                }

                @Override
                public void onFailure(Call<User[]> call, Throwable t) {

                }
            };

    Callback<Prediction[]> fakePredictionCallback =
            new Callback<Prediction[]>() {
                @Override
                public void onResponse(Call<Prediction[]> call, Response<Prediction[]> response) {

                }

                @Override
                public void onFailure(Call<Prediction[]> call, Throwable t) {

                }
            };

    User fakeCurrentUser =
            new User(
                    "id",
                    "username",
                    "url.photo/");


    String fakePlaceId = "id";


    String fakeAutocompleteInput = "fake_input";

    Place.Type[] fakeFilter = new Place.Type[] {Place.Type.RESTAURANT};
}
