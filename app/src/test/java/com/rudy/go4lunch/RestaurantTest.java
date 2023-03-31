package com.rudy.go4lunch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rudy.go4lunch.manager.UserManager;
import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.model.User;
import com.rudy.go4lunch.model.dto.GeometryDto;
import com.rudy.go4lunch.model.dto.LocationDto;
import com.rudy.go4lunch.model.dto.OpeningHoursDto;
import com.rudy.go4lunch.model.dto.PhotoDto;
import com.rudy.go4lunch.viewmodel.MainViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class RestaurantTest {

    LocationDto locationDto = mock(LocationDto.class);
    GeometryDto geometryMock = mock(GeometryDto.class);
    OpeningHoursDto openingHoursMock = mock(OpeningHoursDto.class);
    PhotoDto photoMock = mock(PhotoDto.class);
    List<PhotoDto> photosMock = new ArrayList<>();
    RestaurantDto restaurantMock = mock(RestaurantDto.class);
    String restaurantName = "Restaurant Name";
    String photoRef = "AW30NDwh1ohU9IgpUU0LT8meOf1KFl5Wof5qk7E2ricKUGJY16JaNjsbXW2vL5yTFx4LADpgZ3_FvYIz89gAjhUKh6QaNXVXLbaGT44xnaztKjshPb9nPyyjIHB0QgnkwnE5Ia3x2dBsyCkE__zk-O7zV96gm1BS03KhmDj2je6nqA61Lw8w";
    String placeId = "ChIJKfZA19Hk5EcRVPyjR4GjSss";
    String address = "5 Rue de la Poterne, Orléans";
    String website = "http://example.com";
    String phoneNumber = "0654825469";
    double rating = 4.5;
    double lat = 47.8986733;
    double lng = 1.9093066;
    Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        when(locationDto.getLatitude()).thenReturn(lat);
        when(locationDto.getLongitude()).thenReturn(lng);
        when(geometryMock.getLocationDto()).thenReturn(locationDto);
        when(openingHoursMock.isOpenNow()).thenReturn(true);
        when(photoMock.getPhotoReference()).thenReturn(photoRef);
        photosMock.add(photoMock);
        when(restaurantMock.getGeometry()).thenReturn(geometryMock);
        when(restaurantMock.getOpeningHours()).thenReturn(openingHoursMock);
        when(restaurantMock.getName()).thenReturn(restaurantName);
        when(restaurantMock.getRating()).thenReturn(rating);
        when(restaurantMock.getAddress()).thenReturn(address);
        when(restaurantMock.getPlaceId()).thenReturn(placeId);
        when(restaurantMock.getPhotos()).thenReturn(photosMock);
        when(restaurantMock.getWebsite()).thenReturn(website);
        when(restaurantMock.getFormattedPhoneNumber()).thenReturn(phoneNumber);
    }

    @Test
    public void mockRestaurantTest() {
        assertEquals(geometryMock.getLocationDto().getLatitude(), lat, 0);
        assertEquals(geometryMock.getLocationDto().getLongitude(), lng, 0);
        assertTrue(openingHoursMock.isOpenNow());
        assertEquals(photoMock.getPhotoReference(), photoRef);
        assertEquals(restaurantMock.getGeometry(), geometryMock);
        assertEquals(restaurantMock.getOpeningHours(), openingHoursMock);
        assertEquals(restaurantMock.getName(), restaurantName);
        assertEquals(restaurantMock.getRating(), rating, 0);
        assertEquals(restaurantMock.getAddress(), address);
        assertEquals(restaurantMock.getPlaceId(), placeId);
        assertEquals(restaurantMock.getPhotos(), photosMock);
        assertEquals(restaurantMock.getWebsite(), website);
        assertEquals(restaurantMock.getFormattedPhoneNumber(), phoneNumber);
    }
}
