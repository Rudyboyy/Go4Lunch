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
import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.service.ProcessRestaurantDto;
import com.rudy.go4lunch.viewmodel.MainViewModel;

import java.util.List;

public class MapFragment extends Fragment implements
        OnMapReadyCallback,
        ProcessRestaurantDto {

    private GoogleMap mMap;
    private FragmentMapBinding binding;
    private FusedLocationProviderClient locationClient;
    MainViewModel mViewModel;

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
        mMap = googleMap;
        locationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));
                        mViewModel.getRestaurantLocation(this, location);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        onMapReady(mMap);
    }

    @Override
    public void processRestaurantDto(List<RestaurantDto> restaurantDtoList) {
        for (RestaurantDto result : restaurantDtoList) {
            mMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(result.getGeometry().getLocationDto().getLatitude(), result.getGeometry().getLocationDto().getLongitude()))
                            .title(result.getName())
                            .alpha(0.8f)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }
    }
}