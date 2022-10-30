package com.rudy.go4lunch.ui.restaurant;

import static com.rudy.go4lunch.ui.restaurant.RestaurantsFragment.RESTAURANT_INFO;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rudy.go4lunch.databinding.ActivityDetailRestaurantBinding;
import com.rudy.go4lunch.model.Restaurant;
import com.rudy.go4lunch.model.Workmate;
import com.rudy.go4lunch.ui.workmates.WorkmatesAdapter;

import java.util.ArrayList;

public class DetailRestaurantActivity extends AppCompatActivity {

    ActivityDetailRestaurantBinding binding;
    Restaurant mRestaurant;
    private RecyclerView mRecyclerView;
    private ArrayList<Workmate> workmates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        initData();
        initRecyclerView();
        setRestaurant();
        binding.backButton.setOnClickListener(view -> this.finish());
    }

    private void initUi() {
        binding = ActivityDetailRestaurantBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    private void initRecyclerView() {
        mRecyclerView = binding.recyclerview;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        WorkmatesAdapter mAdapter = new WorkmatesAdapter(workmates);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        workmates = new ArrayList<>(Workmate.getWorkmates());
    }

    @SuppressLint("SetTextI18n")
    public void setRestaurant() {
        mRestaurant = (Restaurant) getIntent().getSerializableExtra(RESTAURANT_INFO);
        binding.restaurantName.setText(mRestaurant.getName());
        binding.address.setText(mRestaurant.getFoodStyle() + " restaurant - " + mRestaurant.getAddress());
    }
}