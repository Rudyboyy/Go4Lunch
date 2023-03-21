package com.rudy.go4lunch.ui.restaurant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.rudy.go4lunch.R;
import com.rudy.go4lunch.model.PredictionsDto;
import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.model.User;
import com.rudy.go4lunch.service.OnSearchListener;
import com.rudy.go4lunch.service.ProcessDetailsRestaurant;
import com.rudy.go4lunch.service.ProcessPredictionsDto;
import com.rudy.go4lunch.service.ProcessRestaurantDto;
import com.rudy.go4lunch.ui.MainActivity;
import com.rudy.go4lunch.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantsFragment extends Fragment implements
        ProcessRestaurantDto,
        OnSearchListener,
        ProcessPredictionsDto,
        ProcessDetailsRestaurant {

    private RecyclerView mRecyclerView;
    private List<RestaurantDto> mRestaurants = new ArrayList<>();
    private List<RestaurantDto> mPredictionRestaurants = new ArrayList<>();
    public static final String RESTAURANT_INFO = "restaurantInfo";
    private MainViewModel mViewModel;
    private FusedLocationProviderClient locationClient;
    private final List<User> mUsers = new ArrayList<>();
    private final List<String> predictionsPlaceId = new ArrayList<>();

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
        ((MainActivity) requireContext()).fragmentSelected = R.layout.fragment_restaurants;
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
                        mViewModel.getRestaurantLocation(this, location, getContext());
                    }
                });
        mViewModel.getDataBaseInstanceUser();
        mViewModel.getAllUsers().observe(requireActivity(), users1 -> {
            mUsers.clear();
            mUsers.addAll(users1);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void processRestaurantDto(List<RestaurantDto> restaurantDtoList) {
        mRestaurants.clear();
        mRestaurants.addAll(restaurantDtoList);
        Objects.requireNonNull(mRecyclerView.getAdapter()).notifyDataSetChanged();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onSearch(String query) {
        locationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null && !query.isEmpty()) {
                        mViewModel.getPredictionLocation(this, location, query);
                    }
                    if (query.isEmpty()) {
                        initList();
                    }
                });
    }

    @Override
    public List<RestaurantDto> onRequestList() {
        return mRestaurants;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.setOnSearchListener(this);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void processPredictionsDto(List<PredictionsDto> predictionsDtoList) {
        predictionsPlaceId.clear();
        for (PredictionsDto predictionsDto : predictionsDtoList) {
            predictionsPlaceId.add(predictionsDto.getPlaceId());
        }
        mViewModel.getPrediction(this, this);
        Objects.requireNonNull(mRecyclerView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public List<String> processDetailsRestaurant() {
        return predictionsPlaceId;
    }
}