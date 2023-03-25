package com.rudy.go4lunch.ui.notification;

import static android.content.Context.MODE_PRIVATE;
import static com.rudy.go4lunch.ui.SettingsActivity.SHARED_PREF_USER_INFO;
import static com.rudy.go4lunch.ui.SettingsActivity.SHARED_PREF_USER_INFO_NOTIFICATION;
import static com.rudy.go4lunch.ui.SettingsActivity.YES;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.rudy.go4lunch.R;
import com.rudy.go4lunch.manager.UserManager;
import com.rudy.go4lunch.model.User;
import com.rudy.go4lunch.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotificationWorker extends Worker {

    private String NOTIFICATION_TAG = "";
    private final UserManager userManager = UserManager.getInstance();
    private String mWorkmatesWhoJoined = "";
    private String mRestaurant;
    private final Context mContext;
    private final String COLLECTION_NAME = "users";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        setNotifications();
        return Result.success();
    }

    private void setNotifications() {
        NOTIFICATION_TAG = mContext.getString(R.string.app_name);
        String statusNotification = mContext.getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                .getString(SHARED_PREF_USER_INFO_NOTIFICATION, "null");
        if (statusNotification.equals(YES)) {
            getWorkmatesWhoHadBooked();
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private void sendVisualNotification() {
        Intent intent = new Intent(mContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = mContext.getString(R.string.channel_id);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(mContext, channelId)
                        .setSmallIcon(R.drawable.restaurant_logo)
                        .setContentTitle(mContext.getString(R.string.app_name))
                        .setContentText(mContext.getString(R.string.you_are_eating_at) + mRestaurant + mWorkmatesWhoJoined)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(mContext.getString(R.string.you_are_eating_at) + mRestaurant + mWorkmatesWhoJoined));

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        // Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = mContext.getString(R.string.channel_name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        // Show notification
        int NOTIFICATION_ID = 7;
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }

    private void getWorkmatesWhoHadBooked() {
        if (userManager.isCurrentUserLogged()) {
            userManager.getUserData().addOnSuccessListener(user -> {
                if (user.getBookedRestaurantPlaceId() != null) {
                    mRestaurant = user.getBookedRestaurant();
                    FirebaseFirestore.getInstance().collection(COLLECTION_NAME).whereNotEqualTo("uid", getCurrentUserUID())
                            .get()
                            .addOnCompleteListener(task -> {
                                List<User> users = new ArrayList<>();
                                for (QueryDocumentSnapshot user1 : task.getResult()) {
                                    users.add(user1.toObject(User.class));
                                }
                                for (User allUser : users) {
                                    if (Objects.equals(allUser.getBookedRestaurantPlaceId(), user.getBookedRestaurantPlaceId())) {
                                        List<String> workmates = new ArrayList<>();
                                        workmates.add(allUser.getUsername());
                                        String joined = String.join(",", workmates);
                                        joined += ".";
                                        mWorkmatesWhoJoined = mContext.getString(R.string.with) + joined;
                                    }
                                }
                                sendVisualNotification();
                            });
                }
            });
        }
    }

    public String getCurrentUserUID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

}
