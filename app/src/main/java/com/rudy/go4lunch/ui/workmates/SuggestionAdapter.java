package com.rudy.go4lunch.ui.workmates;

import android.annotation.SuppressLint;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rudy.go4lunch.R;
import com.rudy.go4lunch.model.RestaurantDto;

import java.util.ArrayList;
import java.util.List;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.ViewHolder> {

    private List<String> data = new ArrayList<>();
    private final List<RestaurantDto> mRestaurantDtoList;
    private final Location mLocation;


    @SuppressLint("NotifyDataSetChanged")
    public SuggestionAdapter(List<RestaurantDto> restaurants, Location location) {
        this.mRestaurantDtoList = restaurants;
        this.mLocation = location;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_predictions, parent, false);
        return new SuggestionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RestaurantDto restaurant = mRestaurantDtoList.get(position);
        holder.bind(restaurant, mLocation);
    }

    @Override
    public int getItemCount() {
        return mRestaurantDtoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView name;
        public final TextView distance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.bestMatch);
            this.distance = itemView.findViewById(R.id.distanceFromCurrentLoc);
        }

        @SuppressLint("SetTextI18n")
        private void bind(RestaurantDto restaurantDto, Location location) {
            name.setText(restaurantDto.getName());

            Location myLoc = new Location("my loc");
            myLoc.setLatitude(location.getLatitude());
            myLoc.setLongitude(location.getLongitude());
            Location locJson = new Location("loc json");
            locJson.setLatitude(restaurantDto.getGeometry().getLocationDto().getLatitude());
            locJson.setLongitude(restaurantDto.getGeometry().getLocationDto().getLongitude());
            int mDistance = (int) myLoc.distanceTo(locJson);

            distance.setText(mDistance + "m");
        }
    }
}
