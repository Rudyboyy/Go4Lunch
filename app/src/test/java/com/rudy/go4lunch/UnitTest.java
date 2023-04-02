package com.rudy.go4lunch;

import static org.junit.Assert.assertEquals;

import android.graphics.Color;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.model.User;
import com.rudy.go4lunch.model.dto.CloseDto;
import com.rudy.go4lunch.model.dto.OpenDto;
import com.rudy.go4lunch.model.dto.OpeningHoursDto;
import com.rudy.go4lunch.model.dto.PeriodsDto;
import com.rudy.go4lunch.utils.StringUtils;
import com.rudy.go4lunch.utils.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class UnitTest {

    @Test
    public void testGetNumberOfWorkmates() {
        List<User> users = new ArrayList<>();
        users.add(new User("0", "user1", "picture"));
        users.add(new User("1", "user2", "picture"));
        users.add(new User("2", "user3", "picture"));
        users.get(0).setBookedRestaurantPlaceId("place1");
        users.get(1).setBookedRestaurantPlaceId("place2");
        users.get(2).setBookedRestaurantPlaceId("place1");

        String result = Utils.getNumberOfWorkmates(users, "place1");

        assertEquals("(2)", result);
    }

    @Test
    public void testGetOpeningHours() {
        List<PeriodsDto> periods = new ArrayList<>();
        periods.add(new PeriodsDto(new CloseDto(1, "2359"), new OpenDto(1, "1900")));
        periods.add(new PeriodsDto(new CloseDto(2, "2359"), new OpenDto(2, "1900")));
        periods.add(new PeriodsDto(new CloseDto(3, "2359"), new OpenDto(3, "1900")));
        periods.add(new PeriodsDto(new CloseDto(4, "2359"), new OpenDto(4, "1900")));
        periods.add(new PeriodsDto(new CloseDto(5, "2359"), new OpenDto(5, "1900")));
        periods.add(new PeriodsDto(new CloseDto(6, "2359"), new OpenDto(6, "1900")));
        periods.add(new PeriodsDto(new CloseDto(7, "2359"), new OpenDto(7, "1900")));
        OpeningHoursDto openingHoursDto = new OpeningHoursDto(true);
        LocalTime now = LocalTime.now();
        if (now.isAfter(LocalTime.parse("23:59")) || now.isBefore(LocalTime.parse("19:00"))) {
            openingHoursDto.setOpenNow(false);
        }
        openingHoursDto.setPeriods(periods);
        RestaurantDto fakeRestaurantDto = new RestaurantDto(null, openingHoursDto, "My Restaurant", 4.5, "Paris, France", "12345", null, "123-456-7890", "https://www.example.com");

        String stringExpected = Utils.getOpeningHours(fakeRestaurantDto, StringUtils::getString);

        if (fakeRestaurantDto.getOpeningHours().isOpenNow()) {
            assertEquals(R.string.open_until + "9:59 PM", stringExpected);
        } else {
            assertEquals(R.string.closed_open_at + "7:00 PM", stringExpected);
        }
    }

    @Test
    public void testGetRestaurantStatus() {
        boolean isOpenNow = true;
        Integer expectedColorOpen = Color.GREEN;
        Integer expectedColorClose = Color.RED;
        Integer resultColorOpen = Utils.getRestaurantStatus(isOpenNow);
        Integer resultColorClose = Utils.getRestaurantStatus(!isOpenNow);
        assertEquals(expectedColorOpen, resultColorOpen);
        assertEquals(expectedColorClose, resultColorClose);
    }
}