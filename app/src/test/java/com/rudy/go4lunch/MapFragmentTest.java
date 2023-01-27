package com.rudy.go4lunch;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.rudy.go4lunch.ui.map.MapFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.reactivex.Single;

@RunWith(MockitoJUnitRunner.class)
public class MapFragmentTest {

    @Mock
    private GoogleMap googleMap;

    @Mock
    private FusedLocationProviderClient locationClient = mock(FusedLocationProviderClient.class);

    private MapFragment fragment;

    @Before
    public void setup() {
        fragment = new MapFragment();
    }

    @Test
    public void onMapReady_movesCameraToLastLocation() {

        double lat = 37.7749;
        double lng = -122.4194;
        LatLng latLng = new LatLng(lat, lng);
        when(locationClient.getLastLocation())
                .thenAnswer(invocation -> Single.fromCallable(() -> new LatLng(lat, lng)));




        fragment.onMapReady(googleMap);

        verify(googleMap).moveCamera(CameraUpdateFactory.newLatLngZoom(eq(latLng), eq(18f)));
        verify(googleMap).setMyLocationEnabled(true);
        verify(googleMap).setOnMarkerClickListener(isNull());
        verify(googleMap).setOnInfoWindowClickListener(isNull());

    }
}

