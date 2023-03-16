package com.rudy.go4lunch.model.dto.predictions;

import com.google.gson.annotations.SerializedName;

public class TermsDto {

    @SerializedName("offset")
    private int offset;

    @SerializedName("value")
    private String value;

    public int getOffset() {
        return offset;
    }

    public String getValue() {
        return value;
    }
}
