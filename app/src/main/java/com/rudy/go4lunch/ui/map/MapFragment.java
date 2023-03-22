package com.rudy.go4lunch.ui.map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rudy.go4lunch.R;
import com.rudy.go4lunch.databinding.FragmentMapBinding;
import com.rudy.go4lunch.model.PredictionsDto;
import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.model.User;
import com.rudy.go4lunch.model.dto.LocationDto;
import com.rudy.go4lunch.service.OnSearchListener;
import com.rudy.go4lunch.service.ProcessDetailsRestaurant;
import com.rudy.go4lunch.service.ProcessPredictionsDto;
import com.rudy.go4lunch.service.ProcessRestaurantDto;
import com.rudy.go4lunch.ui.MainActivity;
import com.rudy.go4lunch.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements
        OnMapReadyCallback,
        ProcessRestaurantDto,
        OnSearchListener,
        ProcessPredictionsDto,
        ProcessDetailsRestaurant {

    private GoogleMap mMap;
    private FragmentMapBinding binding;
    private FusedLocationProviderClient locationClient;
    private MainViewModel mViewModel;
    private final List<RestaurantDto> mRestaurants = new ArrayList<>();
    private final List<String> predictionsPlaceId = new ArrayList<>();
    private boolean onSearch = false;

    @SuppressLint("CheckResult")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        ((MainActivity) requireContext()).fragmentSelected = R.layout.fragment_map;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }


    @SuppressLint({"MissingPermission", "CheckResult"})
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (isAdded()) {
            mMap = googleMap;
            locationClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), location -> {
                        if (location != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));
                            mViewModel.getRestaurantLocation(this, location, getContext());
                        }
                    });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onMapReady(mMap);
        ((MainActivity) requireActivity()).setOnSearchListener(this);
    }

    @Override
    public void processRestaurantDto(List<RestaurantDto> restaurantDtoList) {
        mRestaurants.clear();
        mRestaurants.addAll(restaurantDtoList);
        mViewModel.getDataBaseInstanceUser();
        mViewModel.getAllUsers().observe(getViewLifecycleOwner(), users -> { //todo remplacement getViewLifecycleOwner() par requireActivity car pb dans test
            for (User user : users) {
                for (RestaurantDto result : mRestaurants) {
                    if (result.getPlaceId().equals(user.getBookedRestaurantPlaceId())) {
                        setMarker(BitmapDescriptorFactory.HUE_GREEN, result);// todo pb couleur marker
                    } else if (onSearch) {
                        setMarker(BitmapDescriptorFactory.HUE_YELLOW, result);
                        LocationDto firstLocation = mRestaurants.get(0).getGeometry().getLocationDto();
                        mMap.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(new LatLng(firstLocation.getLatitude(), firstLocation.getLongitude()), 18));
                    } else {
                        setMarker(BitmapDescriptorFactory.HUE_RED, result);
                    }
                }
            }
        });
    }

    private void setMarker(float bitmap, RestaurantDto result) { //todo fait une navigation vers details depuis le marker ?
        mMap.addMarker(
                new MarkerOptions()
                        .position(new LatLng(result.getGeometry().getLocationDto().getLatitude(), result.getGeometry().getLocationDto().getLongitude()))
                        .title(result.getName())
                        .alpha(0.8f)
                        .icon(BitmapDescriptorFactory.defaultMarker(bitmap)));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onSearch(String query) {
        locationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null && !query.isEmpty()) {
                        mViewModel.getPredictionLocation(this, location, query);
                        onSearch = true;
                    }
                    if (query.isEmpty()) {
                        onSearch = false;
                        onMapReady(mMap);
                    }
                });
    }

    @Override
    public List<RestaurantDto> onRequestList() {
        return mRestaurants;
    }

    @Override
    public void processPredictionsDto(List<PredictionsDto> predictionsDtoList) {
        predictionsPlaceId.clear();
        for (PredictionsDto predictionsDto : predictionsDtoList) {
            predictionsPlaceId.add(predictionsDto.getPlaceId());
        }
        mViewModel.getPrediction(this, this);
    }

    @Override
    public List<String> processDetailsRestaurant() {
        return predictionsPlaceId;
    }
}