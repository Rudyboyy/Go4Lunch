package com.rudy.go4lunch.ui.restaurant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        public final ImageView starRate1;
        public final ImageView starRate2;
        public final ImageView starRate3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.restaurant_name);
            this.address = itemView.findViewById(R.id.address);
            this.schedule = itemView.findViewById(R.id.schedule);
            this.attendees = itemView.findViewById(R.id.workmates_number);
            this.starRate1 = itemView.findViewById(R.id.rate_star_1);
            this.starRate2 = itemView.findViewById(R.id.rate_star_2);
            this.starRate3 = itemView.findViewById(R.id.rate_star_3);
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

        public void displayRestaurants(RestaurantDto restaurantDto) {
            name.setText(restaurantDto.getName());
        }
    }
}
