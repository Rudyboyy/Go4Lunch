package com.rudy.go4lunch.ui.restaurant;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.rudy.go4lunch.R;
import com.rudy.go4lunch.manager.UserManager;
import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.model.User;
import com.rudy.go4lunch.service.ProcessRestaurantDto;
import com.rudy.go4lunch.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantsFragment extends Fragment implements
        ProcessRestaurantDto {

    private RecyclerView mRecyclerView;
    private List<RestaurantDto> mRestaurants = new ArrayList<>();
    public static final String RESTAURANT_INFO = "restaurantInfo";
    MainViewModel mViewModel;
    private FusedLocationProviderClient locationClient;
    private UserManager userManager = UserManager.getInstance();
    private List<User> mUsers = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        initList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_restaurants, container, false);
        initList();
        getLocation(root);
        return root;
    }

    private void initRecyclerView(View root, Location location) {
        mRecyclerView = root.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        RestaurantsAdapter mAdapter = new RestaurantsAdapter(mRestaurants, getActivity(), location, mUsers);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(mAdapter);
    }

    @SuppressLint({"MissingPermission", "CheckResult"})
    public void getLocation(View root) { //todo faire une interface pour la location
        locationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        initRecyclerView(root, location);
                    }
                });
    }

    @SuppressLint({"MissingPermission", "CheckResult"})
    public void initList() {
        locationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        mViewModel.getRestaurantLocation(this, location);
                    }
                });
        userManager.getUserData().addOnSuccessListener(user -> {
            mUsers.clear();
            mUsers.add(user);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void processRestaurantDto(List<RestaurantDto> restaurantDtoList) {
        mRestaurants.clear();
        mRestaurants.addAll(restaurantDtoList);
        Objects.requireNonNull(mRecyclerView.getAdapter()).notifyDataSetChanged();
    }

}