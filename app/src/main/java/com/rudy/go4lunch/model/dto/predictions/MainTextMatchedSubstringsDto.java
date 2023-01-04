package com.rudy.go4lunch.model.dto.predictions;

import com.google.gson.annotations.SerializedName;

public class MainTextMatchedSubstringsDto {

    @SerializedName("offset")
    private int offset;

    @SerializedName("length")
    private int length;

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }
}
