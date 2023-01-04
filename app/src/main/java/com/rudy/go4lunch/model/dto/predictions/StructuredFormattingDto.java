package com.rudy.go4lunch.model.dto.predictions;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StructuredFormattingDto {

    @SerializedName("main_text_matched_substrings")
    private List<MainTextMatchedSubstringsDto> mainTextMatchedSubstrings;

    @SerializedName("secondary_text")
    private String secondaryText;

    @SerializedName("main_text")
    private String mainText;

    public List<MainTextMatchedSubstringsDto> getMainTextMatchedSubstrings() {
        return mainTextMatchedSubstrings;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public String getMainText() {
        return mainText;
    }
}
