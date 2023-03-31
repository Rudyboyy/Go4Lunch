package com.rudy.go4lunch.ui.map;

import static com.rudy.go4lunch.ui.restaurant.RestaurantsFragment.RESTAURANT_INFO;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rudy.go4lunch.R;
import com.rudy.go4lunch.databinding.FragmentMapBinding;
import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.model.User;
import com.rudy.go4lunch.model.dto.LocationDto;
import com.rudy.go4lunch.service.OnSearchListener;
import com.rudy.go4lunch.ui.MainActivity;
import com.rudy.go4lunch.ui.restaurant.DetailRestaurantActivity;
import com.rudy.go4lunch.viewmodel.MainViewModel;
import com.rudy.go4lunch.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements
        OnMapReadyCallback,
        OnSearchListener {

    private GoogleMap mMap;
    private FragmentMapBinding binding;
    private FusedLocationProviderClient locationClient;
    private MainViewModel mViewModel;
    private final List<RestaurantDto> mRestaurants = new ArrayList<>();
    private boolean onSearch = false;

    @SuppressLint("CheckResult")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        mViewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance(requireActivity())).get(MainViewModel.class);
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
                            mViewModel.getRestaurantLocation(location);
                        }
                    });
            mViewModel.getRestaurantListLiveData().observe(getViewLifecycleOwner(), restaurantDtos -> {
                mRestaurants.clear();
                mRestaurants.addAll(restaurantDtos);
            });
            setRestaurantMarkerColor();
        }
    }

    public void setRestaurantMarkerColor() {
        List<String> pId = new ArrayList<>();
        mViewModel.getDataBaseInstanceUser();
        mViewModel.getAllUsers().observe(getViewLifecycleOwner(), users -> {
            for (User user : users) {
                if (user.getBookedRestaurantPlaceId() != null) {
                    pId.add(user.getBookedRestaurantPlaceId());
                }
            }
            for (RestaurantDto result : mRestaurants) {
                if (pId.contains(result.getPlaceId())) {
                    setMarker(BitmapDescriptorFactory.HUE_GREEN, result);
                } else if (onSearch) {
                    setMarker(BitmapDescriptorFactory.HUE_YELLOW, mRestaurants.get(0));
                    LocationDto firstLocation = mRestaurants.get(0).getGeometry().getLocationDto();
                    mMap.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(new LatLng(firstLocation.getLatitude(), firstLocation.getLongitude()), 18));
                } else {
                    setMarker(BitmapDescriptorFactory.HUE_RED, result);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        onMapReady(mMap);
        ((MainActivity) requireActivity()).setOnSearchListener(this);
    }

    private void setMarker(float bitmap, RestaurantDto result) {
        Marker marker = mMap.addMarker(
                new MarkerOptions()
                        .position(new LatLng(result.getGeometry().getLocationDto().getLatitude(), result.getGeometry().getLocationDto().getLongitude()))
                        .title(result.getName())
                        .alpha(0.8f)
                        .icon(BitmapDescriptorFactory.defaultMarker(bitmap)));
        if (marker != null) {
            marker.setTag(result);
            mMap.setOnInfoWindowClickListener(this::onMarkerClick);
        }
    }

    public void onMarkerClick(Marker marker) {
        Object tag = marker.getTag();
        if (tag instanceof RestaurantDto) {
            RestaurantDto restaurantDto = (RestaurantDto) tag;
            Intent detailRestaurantActivityIntent = new Intent(getContext(), DetailRestaurantActivity.class);
            detailRestaurantActivityIntent.putExtra(RESTAURANT_INFO, restaurantDto);
            startActivity(detailRestaurantActivityIntent);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onSearch(String query) {
        locationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null && !query.isEmpty()) {
                        mViewModel.getPredictionLocation(location, query).observe(getViewLifecycleOwner(), restaurantDtos -> {
                            mRestaurants.clear();
                            mRestaurants.add(restaurantDtos.get(0));
//                            mRestaurants.addAll(restaurantDtos);
                            onSearch = true;
                        });
                    }
                    if (query.isEmpty()) {
                        onSearch = false;
                        onMapReady(mMap);
                    }
                });
        setRestaurantMarkerColor();
    }

    @Override
    public List<RestaurantDto> onRequestList() {
        return mRestaurants;
    }
}