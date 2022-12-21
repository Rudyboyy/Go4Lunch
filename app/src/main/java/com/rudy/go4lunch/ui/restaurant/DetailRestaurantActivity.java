package com.rudy.go4lunch.ui.restaurant;

import static com.rudy.go4lunch.ui.restaurant.RestaurantsFragment.RESTAURANT_INFO;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.rudy.go4lunch.BuildConfig;
import com.rudy.go4lunch.R;
import com.rudy.go4lunch.databinding.ActivityDetailRestaurantBinding;
import com.rudy.go4lunch.manager.UserManager;
import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.model.User;
import com.rudy.go4lunch.model.Workmate;
import com.rudy.go4lunch.ui.workmates.WorkmatesAdapter;
import com.rudy.go4lunch.ui.workmates.WorkmatesBookingAdapter;
import com.rudy.go4lunch.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailRestaurantActivity extends AppCompatActivity {

    ActivityDetailRestaurantBinding binding;
    RestaurantDto mRestaurant;
    private RecyclerView mRecyclerView;
    private ArrayList<Workmate> workmates;
    private List<User> users = new ArrayList<>();
    private UserManager mUserManager;
    String restaurantId;
    private MainViewModel mViewModel; //todo faire un detail viewmodel??
    private UserManager userManager = UserManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
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
        WorkmatesBookingAdapter mAdapter = new WorkmatesBookingAdapter(users);
        mRecyclerView.setAdapter(mAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initData() {
        mViewModel.getDataBaseInstanceUser();
        mViewModel.getAllUsers().observe(this, users1 -> {
            for (User user : users1) {
                if (Objects.equals(user.getBookedRestaurantPlaceId(), mRestaurant.getPlaceId())) {
                    users.clear();
                    users.add(user);
                }
            }
            Objects.requireNonNull(mRecyclerView.getAdapter()).notifyDataSetChanged();
        });
    }

    @SuppressLint("SetTextI18n")
    public void setRestaurant() {
        mRestaurant = (RestaurantDto) getIntent().getSerializableExtra(RESTAURANT_INFO);

        binding.call.setVisibility(View.INVISIBLE);
        binding.callButton.setVisibility(View.INVISIBLE);
        binding.website.setVisibility(View.INVISIBLE);
        binding.websiteButton.setVisibility(View.INVISIBLE);

        String phoneNumber = mRestaurant.getInternationalPhoneNumber();//todo

        binding.restaurantName.setText(mRestaurant.getName());
        binding.address.setText(mRestaurant.getAddress());

        if (phoneNumber != null) {                           //todo marche pas
            binding.callButton.setOnClickListener(view -> {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:%s" + phoneNumber));
                startActivity(callIntent);
            });
        }

//        binding.likeButton.setOnClickListener();

//        BOOKING BUTTON
        binding.floatingActionButton.setOnClickListener(view -> setBooking());


        binding.ratingBar.setRating(mRestaurant.getCollapseRating());

        if (mRestaurant.getPhotos() != null) {
            Glide.with(binding.restaurantPicture.getContext())
                    .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=100&photo_reference=" +
                            mRestaurant.getPhotos().get(0).getPhotoReference() + "&key=" +
                            BuildConfig.PLACES_API_KEY)
                    .centerCrop()
                    .placeholder(R.drawable.restaurant)
                    .into(binding.restaurantPicture);
        }
    }

    boolean choice;// = Boolean.parseBoolean(null);

    private void updateUIWithUserData() {
//        userManager.updateBooking();
    }

    private void setBookingUserData(FirebaseUser user) {
//        user.
    }

    public void setBooking() {
        @SuppressLint("UseCompatLoadingForDrawables") Drawable myFabSrc = getResources().getDrawable(R.drawable.ic_baseline_check_circle_24);
        Drawable isBooked = myFabSrc.getConstantState().newDrawable();
        isBooked.mutate().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        if (userManager.isCurrentUserLogged()) {
            userManager.getUserData().addOnSuccessListener(user -> {
                if (Objects.equals(user.getBookedRestaurantPlaceId(), mRestaurant.getPlaceId())) {
                    setFab();
                    userManager.cancelBooking();
                } else {
                    setFab();
                    userManager.updateBookedRestaurant(mRestaurant.getName(), mRestaurant.getPlaceId());
                }
            });
        }
    }

    public void setFab() {
        @SuppressLint("UseCompatLoadingForDrawables") Drawable myFabSrc = getResources().getDrawable(R.drawable.ic_baseline_check_circle_24);
        Drawable isBooked = myFabSrc.getConstantState().newDrawable();
        isBooked.mutate().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        if (userManager.isCurrentUserLogged()) {
            userManager.getUserData().addOnSuccessListener(user -> {
                if (Objects.equals(user.getBookedRestaurantPlaceId(), mRestaurant.getPlaceId())) {
                    binding.floatingActionButton.setImageDrawable(isBooked);
                } else {
                    binding.floatingActionButton.setImageResource(R.drawable.ic_baseline_check_circle_24);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRestaurant();
        setFab();
    }
}