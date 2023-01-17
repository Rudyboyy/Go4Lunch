package com.rudy.go4lunch.ui.restaurant;

import static com.rudy.go4lunch.ui.restaurant.RestaurantsFragment.RESTAURANT_INFO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rudy.go4lunch.BuildConfig;
import com.rudy.go4lunch.R;
import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.model.User;

import java.util.List;
import java.util.Locale;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder> {

    private final Context mContext;
    private final List<RestaurantDto> mRestaurantDtoList;
    private final Location mLocation;
    private final List<User> mUsers;

    public RestaurantsAdapter(List<RestaurantDto> restaurants, Context context, Location location, List<User> users) {
        this.mRestaurantDtoList = restaurants;
        this.mContext = context;
        this.mLocation = location;
        this.mUsers = users;
    }

    @NonNull
    @Override
    public RestaurantsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurants, parent, false);
        return new RestaurantsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsAdapter.ViewHolder holder, int position) {
        RestaurantDto restaurant = mRestaurantDtoList.get(position);
        holder.displayRestaurants(restaurant, mLocation, mUsers);

        holder.itemView.setOnClickListener(view -> {
            Intent detailRestaurantActivityIntent = new Intent(mContext, DetailRestaurantActivity.class);
            detailRestaurantActivityIntent.putExtra(RESTAURANT_INFO, restaurant);
            mContext.startActivity(detailRestaurantActivityIntent);
        });
    }

    @Override
    public int getItemCount() {
        return mRestaurantDtoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView name;
        public final TextView address;
        public final TextView schedule;
        public final TextView attendees;
        public final TextView distance;
        public final ImageView itemListPicture;
        public final RatingBar ratingBar;
        public final ConstraintLayout item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.restaurant_name);
            this.address = itemView.findViewById(R.id.address);
            this.schedule = itemView.findViewById(R.id.schedule);
            this.attendees = itemView.findViewById(R.id.workmates_number);
            this.itemListPicture = itemView.findViewById(R.id.item_list_picture);
            this.distance = itemView.findViewById(R.id.distance);
            this.ratingBar = itemView.findViewById(R.id.ratingBar);
            this.item = itemView.findViewById(R.id.item_restaurant);
        }

        private String getRestaurantStatus(Boolean isOpenNow) {
            if (isOpenNow) {
                schedule.setTextColor(Color.GREEN);
                return "Open";
            } else {
                schedule.setTextColor(Color.RED);
                return "Closed";
            }
        }

        @SuppressLint({"CheckResult", "SetTextI18n"})
        public void displayRestaurants(RestaurantDto restaurantDto, Location location, List<User> users) {
            Location myLoc = new Location("my loc");
            myLoc.setLatitude(location.getLatitude());
            myLoc.setLongitude(location.getLongitude());
            Location locJson = new Location("loc json");
            locJson.setLatitude(restaurantDto.getGeometry().getLocationDto().getLatitude());
            locJson.setLongitude(restaurantDto.getGeometry().getLocationDto().getLongitude());
            int mDistance = (int) myLoc.distanceTo(locJson);

            name.setText(restaurantDto.getName());
            address.setText(restaurantDto.getAddress());
            ratingBar.setRating(restaurantDto.getCollapseRating());
            distance.setText(mDistance + "m");
            attendees.setText(getNumberOfWorkmates(users, restaurantDto.getPlaceId()));

            if (restaurantDto.getOpeningHours() != null) {
                schedule.setText(getRestaurantStatus(restaurantDto.getOpeningHours().isOpenNow()));//todo recupérer les horaires avec DetailApi
            }

            if (restaurantDto.getPhotos() != null) {
                Glide.with(itemListPicture.getContext())
                        .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=100&photo_reference=" +
                                restaurantDto.getPhotos().get(0).getPhotoReference() + "&key=" +
                                BuildConfig.PLACES_API_KEY)
                        .centerCrop()
                        .placeholder(R.drawable.restaurant)
                        .into(itemListPicture);
            }
        }

        private String getNumberOfWorkmates(List<User> users, String restaurantPlaceId) {
            int numberOfBookings = 0;
            if (users != null && users.size() > 0) {
                for (User user : users) {
                    if (user.getBookedRestaurantPlaceId().equals(restaurantPlaceId)) {
                        numberOfBookings++;
                    }
                }
            }
            return String.format(Locale.getDefault(), "(%d)", numberOfBookings);
        }
    }
}
