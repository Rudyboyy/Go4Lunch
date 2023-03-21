package com.rudy.go4lunch.service;

import com.rudy.go4lunch.model.RestaurantDto;

import java.util.List;

public interface OnSearchListener {
    void onSearch(String query);
    List<RestaurantDto> onRequestList();
}
