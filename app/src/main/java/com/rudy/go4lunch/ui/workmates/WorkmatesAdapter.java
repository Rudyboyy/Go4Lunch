package com.rudy.go4lunch.ui.workmates;

import android.annotation.SuppressLint;
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
import com.google.firebase.auth.FirebaseUser;
import com.rudy.go4lunch.BuildConfig;
import com.rudy.go4lunch.R;
import com.rudy.go4lunch.manager.UserManager;
import com.rudy.go4lunch.model.User;
import com.rudy.go4lunch.model.Workmate;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesAdapter.ViewHolder> {

    private List<User> mUsers;

    public WorkmatesAdapter(List<User> users) {
        this.mUsers = users;
    }

    @NonNull
    @Override
    public WorkmatesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workmates, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesAdapter.ViewHolder holder, int position) {
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

        @SuppressLint({"SetTextI18n", "ResourceAsColor"})
        public void displayWorkmates(User user) {
            if (user.getChoice()) {
                workmate.setText(user.getUsername() + " is eating ");// + " (" + user.getRestaurant().getName() + ")");
            } else {
                workmate.setText(user.getUsername() + " hasn't decided yet");

                 workmate.setTextColor(Color.GRAY);
                 workmate.setTypeface(workmate.getTypeface(), Typeface.ITALIC);
            }

                if (user.getUrlPicture() != null) {
                    Glide.with(avatar.getContext())
                            .load(user.getUrlPicture())
                            .apply(RequestOptions.circleCropTransform())
                            .into(avatar);
                }
            }
    }
}
