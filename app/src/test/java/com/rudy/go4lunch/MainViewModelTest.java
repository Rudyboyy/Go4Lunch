package com.rudy.go4lunch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import android.location.Location;

import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.model.dto.RestaurantsWrapperDto;
import com.rudy.go4lunch.repository.PredictionRepository;
import com.rudy.go4lunch.repository.RestaurantRepository;
import com.rudy.go4lunch.viewmodel.MainViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

@RunWith(MockitoJUnitRunner.class)
public class MainViewModelTest {


    @Mock
    RestaurantRepository restaurantRepository;

    @Mock
    PredictionRepository predictionRepository;

    private MainViewModel mainViewModel;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mainViewModel = new MainViewModel(restaurantRepository, predictionRepository);
    }

    @Test
    public void getRestaurantLocation_shouldReturnRestaurants() {
        Location location = new Location("");
        List<RestaurantDto> restaurantDtoList = new ArrayList<>();
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDtoList.add(restaurantDto);
        when(restaurantRepository.getGooglePlacesRestaurants(location))
                .thenReturn(Single.just(new RestaurantsWrapperDto(restaurantDtoList)));

        mainViewModel.getRestaurantLocation(location);

        assertNotNull(mainViewModel.getRestaurantListLiveData().getValue());
        assertEquals(1, mainViewModel.getRestaurantListLiveData().getValue().size());
    }
}