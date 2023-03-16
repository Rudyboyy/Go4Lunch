package com.rudy.go4lunch.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PeriodsDto implements Serializable {

    @SerializedName("close")
    CloseDto close;

    @SerializedName("open")
    OpenDto open;

    public PeriodsDto(CloseDto close, OpenDto open) {
        this.close = close;
        this.open = open;
    }

    public CloseDto getClose() {
        return close;
    }

    public void setClose(CloseDto close) {
        this.close = close;
    }

    public OpenDto getOpen() {
        return open;
    }

    public void setOpen(OpenDto open) {
        this.open = open;
    }
}
