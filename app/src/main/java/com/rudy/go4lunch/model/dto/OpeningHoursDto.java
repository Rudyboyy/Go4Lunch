package com.rudy.go4lunch.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OpeningHoursDto implements Serializable  {

    @SerializedName("open_now")
    boolean openNow;

    @SerializedName("weekday_text")
    List<String> weekDay;

    @SerializedName("periods")
    List<PeriodsDto> periods;

    public List<String> getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(List<String> weekDay) {
        this.weekDay = weekDay;
    }

    public List<PeriodsDto> getPeriods() {
        return periods;
    }

    public void setPeriods(List<PeriodsDto> periods) {
        this.periods = periods;
    }

    public OpeningHoursDto(boolean openNow) {
        this.openNow = openNow;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }
}
