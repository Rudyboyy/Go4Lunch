package com.rudy.go4lunch.model.dto;

import com.google.gson.annotations.SerializedName;

public class PhotoDto {

    @SerializedName("height")
    int height;

    @SerializedName("width")
    int width;

    @SerializedName("photo_reference")
    String photoReference;

    public PhotoDto(int height, int width, String photoReference) {
        this.height = height;
        this.width = width;
        this.photoReference = photoReference;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }
}
