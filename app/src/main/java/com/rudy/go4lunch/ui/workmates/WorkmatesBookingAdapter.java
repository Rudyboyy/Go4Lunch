package com.rudy.go4lunch.ui.workmates;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rudy.go4lunch.R;
import com.rudy.go4lunch.manager.UserManager;
import com.rudy.go4lunch.model.User;

import java.util.List;

public class WorkmatesBookingAdapter extends RecyclerView.Adapter<WorkmatesBookingAdapter.ViewHolder>{

    private List<User> mUsers;

    public WorkmatesBookingAdapter(List<User> users) {
        this.mUsers = users;
    }


    @NonNull
    @Override
    public WorkmatesBookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workmates, parent, false);
        return new WorkmatesBookingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesBookingAdapter.ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.displayWorkmates(user);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView avatar;
        public final TextView workmate;
        private UserManager userManager = UserManager.getInstance();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.avatar = itemView.findViewById(R.id.item_list_avatar);
            this.workmate = itemView.findViewById(R.id.workmate_text_view);
        }
        public void displayWorkmates(User user) {
                workmate.setText(user.getUsername());

            if (user.getUrlPicture() != null) {
                Glide.with(avatar.getContext())
                        .load(user.getUrlPicture())
                        .apply(RequestOptions.circleCropTransform())
                        .into(avatar);
            }
        }
    }
}
