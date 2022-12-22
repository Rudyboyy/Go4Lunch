package com.rudy.go4lunch.model;

import com.google.gson.annotations.SerializedName;
import com.rudy.go4lunch.model.dto.GeometryDto;
import com.rudy.go4lunch.model.dto.OpeningHoursDto;
import com.rudy.go4lunch.model.dto.PhotoDto;

import java.io.Serializable;
import java.util.List;

public class RestaurantDto implements Serializable {

    @SerializedName("geometry")
    GeometryDto geometry;

    @SerializedName("opening_hours")
    OpeningHoursDto openingHours;

    @SerializedName("name")
    String name;

    @SerializedName("rating")
    double rating;

    @SerializedName("vicinity")
    String address;

    @SerializedName("place_id")
    String placeId;

    @SerializedName("photos")
    List<PhotoDto> photos;

    @SerializedName("international_phone_number") //todo marche pas
    String internationalPhoneNumber;

    public RestaurantDto(GeometryDto geometry, OpeningHoursDto openingHours, String name, double rating, String address, String placeId, List<PhotoDto> photos, String internationalPhoneNumber) {
        this.geometry = geometry;
        this.openingHours = openingHours;
        this.name = name;
        this.rating = rating;
        this.address = address;
        this.placeId = placeId;
        this.photos = photos;
        this.internationalPhoneNumber = internationalPhoneNumber;
    }

    public GeometryDto getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometryDto geometry) {
        this.geometry = geometry;
    }

    public OpeningHoursDto getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHoursDto openingHours) {
        this.openingHours = openingHours;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double ratting) {
        this.rating = ratting;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public List<PhotoDto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoDto> photos) {
        this.photos = photos;
    }

    public float getCollapseRating() {
        return (float) ((rating / 5) * 3);
    }

    public String getInternationalPhoneNumber() {
        return internationalPhoneNumber;
    }

    public void setInternationalPhoneNumber(String internationalPhoneNumber) {
        this.internationalPhoneNumber = internationalPhoneNumber;
    }
}
