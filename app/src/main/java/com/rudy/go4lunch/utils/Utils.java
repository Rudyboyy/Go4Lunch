package com.rudy.go4lunch.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;

import androidx.test.core.app.ApplicationProvider;

import com.rudy.go4lunch.R;
import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.model.User;
import com.rudy.go4lunch.model.dto.OpeningHoursDto;
import com.rudy.go4lunch.model.dto.PeriodsDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static String getNumberOfWorkmates(List<User> users, String restaurantPlaceId) {
        int numberOfBookings = 0;
        if (users != null && users.size() > 0) {
            for (User user : users) {
                if (user.getBookedRestaurantPlaceId() != null && user.getBookedRestaurantPlaceId().equals(restaurantPlaceId)) {
                    numberOfBookings++;
                }
            }
        }
        return String.format(Locale.getDefault(), "(%d)", numberOfBookings);
    }

    public static String getOpeningHours(RestaurantDto restaurantDto, Context context) {
        String status = "";
        OpeningHoursDto openingHoursDto = restaurantDto.getOpeningHours();
        if (openingHoursDto.getPeriods() != null) {
            List<PeriodsDto> periodsDtoList = openingHoursDto.getPeriods();
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat  = new SimpleDateFormat("HHmm");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat  = new SimpleDateFormat("HH:mm");
            for (PeriodsDto period : periodsDtoList) {
                if (period.getOpen().getDay() == day) {
                    try {
                        Date openTime = inputFormat.parse(period.getOpen().getTime());
                        Date closeTime = inputFormat.parse(period.getClose().getTime());
                        assert openTime != null;
                        assert closeTime != null;
                        if (restaurantDto.getOpeningHours().isOpenNow()) {
                            status = context.getString(R.string.open_until) + outputFormat.format(closeTime);
                        } else {
                            status = context.getString(R.string.close_open_at) + outputFormat.format(openTime);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (restaurantDto.getOpeningHours().isOpenNow()) {
            status = context.getString(R.string.open);
        } else {
            status = context.getString(R.string.close);
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
