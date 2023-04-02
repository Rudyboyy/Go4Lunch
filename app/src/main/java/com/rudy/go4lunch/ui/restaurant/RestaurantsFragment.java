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
import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.model.User;
import com.rudy.go4lunch.service.OnSearchListener;
import com.rudy.go4lunch.ui.MainActivity;
import com.rudy.go4lunch.viewmodel.MainViewModel;
import com.rudy.go4lunch.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantsFragment extends Fragment implements OnSearchListener {

    private RecyclerView mRecyclerView;
    private final List<RestaurantDto> mRestaurants = new ArrayList<>();
    public static final String RESTAURANT_INFO = "restaurantInfo";
    private MainViewModel mViewModel;
    private FusedLocationProviderClient locationClient;
    private final List<User> mUsers = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        mViewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance(requireActivity())).get(MainViewModel.class);
        initList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_restaurants, container, false);
        getLocation(root);
        initList();
        ((MainActivity) requireContext()).fragmentSelected = R.layout.fragment_restaurants;
        return root;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initRecyclerView(View root, Location location) {
        mRecyclerView = root.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        RestaurantsAdapter mAdapter = new RestaurantsAdapter(mRestaurants, getActivity(), location, mUsers);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(mAdapter);
        Objects.requireNonNull(mRecyclerView.getAdapter()).notifyDataSetChanged();
    }

    @SuppressLint({"MissingPermission", "CheckResult"})
    public void getLocation(View root) {
        locationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        initRecyclerView(root, location);
                    }
                });
    }

    @SuppressLint({"MissingPermission", "CheckResult", "NotifyDataSetChanged"})
    public void initList() {
        mViewModel.getRestaurantListLiveData().observe(requireActivity(), restaurantDtos -> {
            mRestaurants.clear();
            mRestaurants.addAll(restaurantDtos);
        });
        mViewModel.getDataBaseInstanceUser();
        mViewModel.getAllUsers().observe(requireActivity(), users1 -> {
            mUsers.clear();
            mUsers.addAll(users1);
        });
    }

    @SuppressLint({"MissingPermission", "NotifyDataSetChanged"})
    @Override
    public void onSearch(String query) {
        locationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null && !query.isEmpty()) {
                        mViewModel.getPredictionLocation(location, query).observe(requireActivity(), restaurantDtos -> {
                            mRestaurants.clear();
                            mRestaurants.addAll(restaurantDtos);
                            Objects.requireNonNull(mRecyclerView.getAdapter()).notifyDataSetChanged();
                        });
                    }
                    if (query.isEmpty()) {
                        mViewModel.getRestaurantListLiveData().observe(requireActivity(), restaurantDtos -> {
                            mRestaurants.clear();
                            mRestaurants.addAll(restaurantDtos);
                            Objects.requireNonNull(mRecyclerView.getAdapter()).notifyDataSetChanged();
                        });
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).setOnSearchListener(this);
    }
}