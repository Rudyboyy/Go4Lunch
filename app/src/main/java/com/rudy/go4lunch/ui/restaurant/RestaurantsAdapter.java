package com.rudy.go4lunch.ui.restaurant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rudy.go4lunch.BuildConfig;
import com.rudy.go4lunch.R;
import com.rudy.go4lunch.model.Restaurant;
import com.rudy.go4lunch.model.RestaurantDto;

import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder> {

    private final Context mContext;
    private final List<RestaurantDto> mRestaurantDtoList;

    public RestaurantsAdapter(List<RestaurantDto> restaurants, Context context) {
        this.mRestaurantDtoList = restaurants;
        this.mContext = context;
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
        holder.displayRestaurants(mRestaurantDtoList.get(position));

//        holder.itemView.setOnClickListener(view -> {
//            Intent detailRestaurantActivityIntent = new Intent(mContext, DetailRestaurantActivity.class);
//            detailRestaurantActivityIntent.putExtra(RESTAURANT_INFO, restaurant);
//            mContext.startActivity(detailRestaurantActivityIntent);
//        });
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
        public final ImageView starRate1;
        public final ImageView starRate2;
        public final ImageView starRate3;
        public final ImageView itemListPicture;
        public final RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.restaurant_name);
            this.address = itemView.findViewById(R.id.address);
            this.schedule = itemView.findViewById(R.id.schedule);
            this.attendees = itemView.findViewById(R.id.workmates_number);
            this.starRate1 = itemView.findViewById(R.id.rate_star_1);//todo ???
            this.starRate2 = itemView.findViewById(R.id.rate_star_2);//todo ???
            this.starRate3 = itemView.findViewById(R.id.rate_star_3);//todo ???
            this.itemListPicture = itemView.findViewById(R.id.item_list_picture);
            this.distance = itemView.findViewById(R.id.distance);
            this.ratingBar = itemView.findViewById(R.id.ratingBar);
        }

        @SuppressLint("SetTextI18n")
        public void displayRestaurants(Restaurant restaurant) {
            name.setText(restaurant.getName());
            address.setText(restaurant.getFoodStyle() + " - " + restaurant.getAddress());
//            attendees.setText("(" + restaurant.getWorkmatesNumber() + ")");

            switch (restaurant.getStarRate()) {
                case 1:
                    starRate1.setImageResource(R.drawable.ic_baseline_star_24);
                    break;
                case 2:
                    starRate1.setImageResource(R.drawable.ic_baseline_star_24);
                    starRate2.setImageResource(R.drawable.ic_baseline_star_24);
                    break;
                case 3:
                    starRate1.setImageResource(R.drawable.ic_baseline_star_24);
                    starRate2.setImageResource(R.drawable.ic_baseline_star_24);
                    starRate3.setImageResource(R.drawable.ic_baseline_star_24);
                    break;
            }
        }

        private int getRating(Double rating) {
            return (int) ((rating / 5) * 3);
        }

        private String getOpeningTime(Boolean isOpenNow) {
            if (isOpenNow) {
                schedule.setTextColor(Color.GREEN);
                return "Open now";
            } else {
                schedule.setTextColor(Color.RED);
                return "Closed";
            }
        }

        @SuppressLint("CheckResult")
        public void displayRestaurants(RestaurantDto restaurantDto) {
            name.setText(restaurantDto.getName());
            address.setText(restaurantDto.getAddress());
            ratingBar.setRating(getRating(restaurantDto.getRating()));

            if (restaurantDto.getOpeningHours() != null) {
                schedule.setText(getOpeningTime(restaurantDto.getOpeningHours().isOpenNow()));                       //todo ajouter au layout rating star
            }

            if (restaurantDto.getPhotos() != null) {
                Glide.with(itemListPicture.getContext())
                        .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=100&photo_reference="
                                + restaurantDto.getPhotos().get(0).getPhotoReference() + "&key=" + BuildConfig.PLACES_API_KEY)
                        .centerCrop()
                        .placeholder(R.drawable.restaurant)
                        .into(itemListPicture);
            }
        }
    }
}
