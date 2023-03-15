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
    Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        when(locationDto.getLatitude()).thenReturn(47.8986733);
        when(locationDto.getLongitude()).thenReturn(1.9093066);
        when(geometryMock.getLocationDto()).thenReturn(locationDto);
        when(openingHoursMock.isOpenNow()).thenReturn(true);
        when(photoMock.getPhotoReference()).thenReturn("AW30NDwh1ohU9IgpUU0LT8meOf1KFl5Wof5qk7E2ricKUGJY16JaNjsbXW2vL5yTFx4LADpgZ3_FvYIz89gAjhUKh6QaNXVXLbaGT44xnaztKjshPb9nPyyjIHB0QgnkwnE5Ia3x2dBsyCkE__zk-O7zV96gm1BS03KhmDj2je6nqA61Lw8w");
        photosMock.add(photoMock);
        when(restaurantMock.getGeometry()).thenReturn(geometryMock);
        when(restaurantMock.getOpeningHours()).thenReturn(openingHoursMock);
        when(restaurantMock.getName()).thenReturn("Restaurant Name");
        when(restaurantMock.getRating()).thenReturn(4.5);
        when(restaurantMock.getAddress()).thenReturn("5 Rue de la Poterne, Orléans");
        when(restaurantMock.getPlaceId()).thenReturn("ChIJKfZA19Hk5EcRVPyjR4GjSss");
        when(restaurantMock.getPhotos()).thenReturn(photosMock);
        when(restaurantMock.getWebsite()).thenReturn("http://example.com");
        when(restaurantMock.getFormattedPhoneNumber()).thenReturn("0654825469");
    }

    @Test
    public void mockTest() {
        assertEquals(geometryMock.getLocationDto().getLatitude(), 47.8986733, 0);
        assertEquals(geometryMock.getLocationDto().getLongitude(), 1.9093066, 0);
        assertTrue(openingHoursMock.isOpenNow());
        assertEquals(photoMock.getPhotoReference(), "AW30NDwh1ohU9IgpUU0LT8meOf1KFl5Wof5qk7E2ricKUGJY16JaNjsbXW2vL5yTFx4LADpgZ3_FvYIz89gAjhUKh6QaNXVXLbaGT44xnaztKjshPb9nPyyjIHB0QgnkwnE5Ia3x2dBsyCkE__zk-O7zV96gm1BS03KhmDj2je6nqA61Lw8w");
        assertEquals(restaurantMock.getGeometry(), geometryMock);
        assertEquals(restaurantMock.getOpeningHours(), openingHoursMock);
        assertEquals(restaurantMock.getName(), "Restaurant Name");
        assertEquals(restaurantMock.getRating(), 4.5, 0);
        assertEquals(restaurantMock.getAddress(), "5 Rue de la Poterne, Orléans");
        assertEquals(restaurantMock.getPlaceId(), "ChIJKfZA19Hk5EcRVPyjR4GjSss");
        assertEquals(restaurantMock.getPhotos(), photosMock);
        assertEquals(restaurantMock.getWebsite(), "http://example.com");
        assertEquals(restaurantMock.getFormattedPhoneNumber(), "0654825469");
    }

    public static DocumentSnapshot getDocumentSnapshotMock() {
        User user = new User("user123", "John", "https://example.com/profile.jpg");
        user.setBookedRestaurant("Pizza Hut");
        user.setBookedRestaurantPlaceId("123456");
        user.setFavoriteRestaurants(Arrays.asList("Burger King", "McDonald's"));

        DocumentSnapshot documentSnapshot = Mockito.mock(DocumentSnapshot.class);
        Mockito.when(documentSnapshot.toObject(User.class)).thenReturn(user);
        return documentSnapshot;
    }

    public static QuerySnapshot getQuerySnapshotMock() {
        User user1 = new User("user123", "John", "https://example.com/profile.jpg");
        user1.setBookedRestaurant("Pizza Hut");
        user1.setBookedRestaurantPlaceId("123456");
        user1.setFavoriteRestaurants(Arrays.asList("Burger King", "McDonald's"));

        User user2 = new User("user456", "Jane", "https://example.com/profile.jpg");
        user2.setBookedRestaurant("McDonald's");
        user2.setBookedRestaurantPlaceId("789012");
        user2.setFavoriteRestaurants(Collections.singletonList("Pizza Hut"));

        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
        Mockito.when(querySnapshot.toObjects(User.class)).thenReturn(Arrays.asList(user1, user2));
        return querySnapshot;
    }
}
