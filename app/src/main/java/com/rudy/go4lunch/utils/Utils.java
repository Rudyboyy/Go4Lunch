package com.rudy.go4lunch.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

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
import java.util.TimeZone;

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
        String status;
        String openCloseStatus = "";
        String time = "";
        Calendar calendar = Calendar.getInstance();
        Calendar closeCalendar = Calendar.getInstance();
        Calendar openCalendar = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        calendar.setTimeZone(timeZone);
        int today = calendar.get(Calendar.DAY_OF_WEEK);
        int tomorrow = today + 1;
        if (tomorrow > 7) {
            tomorrow = 1;
        }
        int loopCount = 0;
        int tryCatch = 0;
        boolean isOpen = restaurantDto.getOpeningHours().isOpenNow();
        boolean periodFound = false;
        boolean firstLoopExecuted = false;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("HHmm");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
        OpeningHoursDto openingHoursDto = restaurantDto.getOpeningHours();

        if (openingHoursDto.getPeriods() != null && !openingHoursDto.getPeriods().isEmpty()) {
            List<PeriodsDto> periodsDtoList = openingHoursDto.getPeriods();
            while (loopCount < 2) {
                for (PeriodsDto period : periodsDtoList) {
                    try {
                        int openDay = period.getOpen().getDay();
                        Date openTime = inputFormat.parse(period.getOpen().getTime());
                        Date closeTime = inputFormat.parse(period.getClose().getTime());
                        assert openTime != null;
                        assert closeTime != null;
                        closeCalendar.setTime(closeTime);
                        openCalendar.setTime(openTime);
                        closeCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
                        openCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
                        closeCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                        openCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                        closeCalendar.set(openDay, calendar.get(Calendar.DAY_OF_WEEK));
                        openCalendar.set(openDay, calendar.get(Calendar.DAY_OF_WEEK));
                        closeCalendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
                        openCalendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
                        periodFound = true;

                        if (openDay == today && !firstLoopExecuted) {
                            if (isOpen && closeCalendar.getTime().after(calendar.getTime())) {
                                openCloseStatus = context.getString(R.string.open_until);
                                time = outputFormat.format(closeTime);
                                loopCount++;
                                break;
                            } else if (!isOpen && openCalendar.getTime().after(calendar.getTime())) {
                                if (closeCalendar.getTime().after(calendar.getTime()) || openCalendar.getTime().after(calendar.getTime())) {
                                    openCloseStatus = context.getString(R.string.closed_open_at);
                                    time = outputFormat.format(openTime);
                                    loopCount++;
                                    break;
                                }
                            }
                        } else if (firstLoopExecuted) {
                            for (int i = 1; i <= 7; i++) {
                                if (openDay == tomorrow || i == openDay && i > today) {
                                    if (isOpen) {
                                        openCloseStatus = context.getString(R.string.open_until) + getDayOfWeek(context, openDay);
                                        time = outputFormat.format(closeTime);
                                        if (openDay == tomorrow) {
                                            openCloseStatus = context.getString(R.string.open_until) + context.getString(R.string.tomorrow);
                                        }
                                    } else {
                                        openCloseStatus = context.getString(R.string.closed_open) + getDayOfWeek(context, openDay);
                                        time = outputFormat.format(openTime);
                                        if (openDay == tomorrow) {
                                            openCloseStatus = context.getString(R.string.closed_open) + context.getString(R.string.tomorrow_at);
                                        }
                                    }
                                    break;
                                }
                            }
                            if (!openCloseStatus.isEmpty()) {
                                break;
                            }
                            loopCount++;
                        }
                        if (periodsDtoList.indexOf(period) + 1 == periodsDtoList.size()) {
                            firstLoopExecuted = true;
                            loopCount++;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        tryCatch++;
                        if (tryCatch >= 7) {
                            loopCount++;
                        }
                    }
                }
            }
        }
        if (!periodFound || time.isEmpty() || openCloseStatus.isEmpty()) {
            if (openingHoursDto.isOpenNow()) {
                status = context.getString(R.string.open);
            } else {
                status = context.getString(R.string.close);
            }
        } else {
            status = openCloseStatus + time;
        }
        return status;
    }

    private static String getDayOfWeek(Context context, int day) {
        switch (day) {
            case Calendar.MONDAY:
                return context.getString(R.string.monday);
            case Calendar.TUESDAY:
                return context.getString(R.string.tuesday);
            case Calendar.WEDNESDAY:
                return context.getString(R.string.wednesday);
            case Calendar.THURSDAY:
                return context.getString(R.string.thursday);
            case Calendar.FRIDAY:
                return context.getString(R.string.friday);
            case Calendar.SATURDAY:
                return context.getString(R.string.saturday);
            case Calendar.SUNDAY:
                return context.getString(R.string.sunday);
        }
        return "";
    }


    public static int getRestaurantStatus(Boolean isOpenNow) {
        if (isOpenNow) {
            return Color.GREEN;
        } else {
            return Color.RED;
        }
    }
}
