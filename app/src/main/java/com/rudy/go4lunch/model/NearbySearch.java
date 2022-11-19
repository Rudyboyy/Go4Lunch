package com.rudy.go4lunch.model;

import android.location.Location;

import com.google.geo.type.Viewport;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NearbySearch {

    class Geometry {

        @SerializedName("location")
        @Expose
        private Location location;

        public Location getLocation() {
            return location;
        }
    }
}
