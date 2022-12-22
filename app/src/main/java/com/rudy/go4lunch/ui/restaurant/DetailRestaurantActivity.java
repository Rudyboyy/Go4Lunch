package com.rudy.go4lunch.ui.restaurant;

import static com.rudy.go4lunch.ui.restaurant.RestaurantsFragment.RESTAURANT_INFO;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.rudy.go4lunch.BuildConfig;
import com.rudy.go4lunch.R;
import com.rudy.go4lunch.databinding.ActivityDetailRestaurantBinding;
import com.rudy.go4lunch.manager.UserManager;
import com.rudy.go4lunch.model.Restaurant;
import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.model.User;
import com.rudy.go4lunch.model.Workmate;
import com.rudy.go4lunch.ui.workmates.WorkmatesAdapter;
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
//        initData();
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
        WorkmatesAdapter mAdapter = new WorkmatesAdapter(users);
        mRecyclerView.setAdapter(mAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initData() { //todo init users who has booked
        mViewModel.getDataBaseInstanceUser();
        mViewModel.getAllUsers().observe(this, users1 -> {
            users.clear();
            users.addAll(users1);
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

//        binding.floatingActionButton.setOnClickListener(view -> updateUserChoice());



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

    private void updateUserChoice(){
        if(userManager.isCurrentUserLogged()){
//            userManager.getUserData().addOnSuccessListener(user -> user.setChoice(true));
            userManager.updateChoice(!choice);
        }
    }
}