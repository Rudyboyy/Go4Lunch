package com.rudy.go4lunch.model.dto.predictions;

import com.google.gson.annotations.SerializedName;
import com.rudy.go4lunch.model.PredictionsDto;

import java.util.List;

public class AutoCompleteDto {

    @SerializedName("predictions")
    private List<PredictionsDto> predictions;

    @SerializedName("status")
    private String status;

    public List<PredictionsDto> getPredictions() {
        return predictions;
    }

    public String getStatus() {
        return status;
    }
}
