package com.rudy.go4lunch.model;

import com.google.gson.annotations.SerializedName;
import com.rudy.go4lunch.model.dto.predictions.MainTextMatchedSubstringsDto;
import com.rudy.go4lunch.model.dto.predictions.StructuredFormattingDto;
import com.rudy.go4lunch.model.dto.predictions.TermsDto;

import java.util.List;

public class PredictionsDto {

    @SerializedName("reference")
    private String reference;

    @SerializedName("types")
    private List<String> types;

    @SerializedName("matched_substrings")
    private List<MainTextMatchedSubstringsDto> matchedSubstrings;

    @SerializedName("terms")
    private List<TermsDto> terms;

    @SerializedName("structured_formatting")
    private StructuredFormattingDto structuredFormatting;

    @SerializedName("description")
    private String description;

    @SerializedName("place_id")
    private String placeId;

    public String getReference() {
        return reference;
    }

    public List<String> getTypes() {
        return types;
    }

    public List<MainTextMatchedSubstringsDto> getMatchedSubstrings() {
        return matchedSubstrings;
    }

    public List<TermsDto> getTerms() {
        return terms;
    }

    public StructuredFormattingDto getStructuredFormatting() {
        return structuredFormatting;
    }

    public String getDescription() {
        return description;
    }

    public String getPlaceId() {
        return placeId;
    }
}
