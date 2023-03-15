package com.rudy.go4lunch.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;

import androidx.test.core.app.ApplicationProvider;

import com.rudy.go4lunch.R;
import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.model.User;
import com.rudy.go4lunch.model.dto.PeriodsDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {

//    static Context context = ApplicationProvider.getApplicationContext();
    public static String openUntil = String.valueOf(R.string.open_until);
    public static String closedOpenAt = String.valueOf(R.string.close_open_at);

    public static String getNumberOfWorkmates(List<User> users, String restaurantPlaceId) {
        int numberOfBookings = 0;
//        if (users != null && users.size() > 0) { todo ne marche pas
//            for (User user : users) {
//                if (user.getBookedRestaurantPlaceId().equals(restaurantPlaceId)) {
//                    numberOfBookings++;
//                }
//            }
//        }
//        return String.format(Locale.getDefault(), "(%d)", numberOfBookings);
        return String.format(Locale.getDefault(), "(%d)", numberOfBookings);
    }

    public static String getOpeningHours(RestaurantDto restaurantDto) {
        String status = "";
        List<PeriodsDto> periodsDtoList = restaurantDto.getOpeningHours().getPeriods();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        for (PeriodsDto period : periodsDtoList) {
            if (period.getOpen().getDay() == day) {
                try {
                    Date openTime = timeFormat.parse(period.getOpen().getTime());
                    Date closeTime = timeFormat.parse(period.getClose().getTime());
                    assert openTime != null;
                    assert closeTime != null;
                    if (restaurantDto.getOpeningHours().isOpenNow()) {
//                        status = openUntil + timeFormat.format(closeTime);
                        status = R.string.open_until + timeFormat.format(closeTime);
                    } else {
//                        status = closedOpenAt + timeFormat.format(openTime);
//                        status = context.getResources().getString(R.string.close_open_at) + timeFormat.format(openTime);
                        status = R.string.close_open_at + timeFormat.format(openTime);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (!restaurantDto.getOpeningHours().isOpenNow()) {
                status = String.valueOf(R.string.close);
            } else {
                status = String.valueOf(R.string.open);
            }
        }
        return status;
    }

    public static int getRestaurantStatus(Boolean isOpenNow) {
        if (isOpenNow) {
            return Color.GREEN;
        } else {
            return Color.RED;
        }
    }
}
