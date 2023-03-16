package com.rudy.go4lunch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import android.content.Context;
import android.graphics.Color;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.model.User;
import com.rudy.go4lunch.model.dto.CloseDto;
import com.rudy.go4lunch.model.dto.OpenDto;
import com.rudy.go4lunch.model.dto.OpeningHoursDto;
import com.rudy.go4lunch.model.dto.PeriodsDto;
import com.rudy.go4lunch.ui.MainActivity;
import com.rudy.go4lunch.utils.Utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UnitTest {

//    Context context = mock(Context.class);
    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

//    @Before
//    public void setUp() {
//        context = ApplicationProvider.getApplicationContext();
//       // ou ApplicationProvider.getApplicationContext()
//    }

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
    public void testGetOpeningHours() throws Exception {
        List<PeriodsDto> periods = new ArrayList<>();
        periods.add(new PeriodsDto(new CloseDto(1, "23:59"), new OpenDto(1, "19:00")));
        periods.add(new PeriodsDto(new CloseDto(2, "23:59"), new OpenDto(2, "19:00")));
        periods.add(new PeriodsDto(new CloseDto(3, "23:59"), new OpenDto(3, "19:00")));
        periods.add(new PeriodsDto(new CloseDto(4, "23:59"), new OpenDto(4, "19:00")));
        periods.add(new PeriodsDto(new CloseDto(5, "23:59"), new OpenDto(5, "19:00")));
        periods.add(new PeriodsDto(new CloseDto(6, "23:59"), new OpenDto(6, "19:00")));
        periods.add(new PeriodsDto(new CloseDto(7, "23:59"), new OpenDto(7, "19:00")));
        OpeningHoursDto openingHoursDto = new OpeningHoursDto(true);
        LocalTime now = LocalTime.now();
        if (now.isAfter(LocalTime.parse("23:59")) || now.isBefore(LocalTime.parse("19:00"))) {
            openingHoursDto.setOpenNow(false);
        }
        openingHoursDto.setPeriods(periods);
        RestaurantDto fakeRestaurantDto = new RestaurantDto(null, openingHoursDto, "My Restaurant", 4.5, "Paris, France", "12345", null, "123-456-7890", "https://www.example.com");

        String stringExpected = Utils.getOpeningHours(fakeRestaurantDto, context);

        if (fakeRestaurantDto.getOpeningHours().isOpenNow()) {
            assertEquals(R.string.open_until + "23:59", stringExpected);
        } else {
            assertEquals(R.string.close_open_at + "19:00", stringExpected);
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

    //test les viewmodels avec mockito
}