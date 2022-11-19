package com.rudy.go4lunch.model;

import android.location.Location;

import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Restaurants {

    @SerializedName("location")
    @Expose
    public Location location;

    @SerializedName("icon")
    @Expose
    public String icon;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("opening_hours")
    @Expose
    public OpeningHours openingHours;

    @SerializedName("open_now")
    @Expose
    public boolean openNow;

    @SerializedName("height")
    @Expose
    public int height;

    @SerializedName("width")
    @Expose
    public int width;

    @SerializedName("html_attributions")
    @Expose
    public String htmlAttributions; //LIST ?

    @SerializedName("photo_reference")
    @Expose
    public String photoReference;

    @SerializedName("place_id")
    @Expose
    public String placeId;

    @SerializedName("rating")
    @Expose
    public double rating;

    @SerializedName("types")
    @Expose
    public List<String> types;

    @SerializedName("vicinity")
    @Expose
    public String vicinity;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
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

    public String getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(String htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }
}
