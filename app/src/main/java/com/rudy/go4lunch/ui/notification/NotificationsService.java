package com.rudy.go4lunch.ui.notification;

import static com.rudy.go4lunch.ui.SettingsActivity.NO;
import static com.rudy.go4lunch.ui.SettingsActivity.SHARED_PREF_USER_INFO;
import static com.rudy.go4lunch.ui.SettingsActivity.SHARED_PREF_USER_INFO_NOTIFICATION;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rudy.go4lunch.R;
import com.rudy.go4lunch.manager.UserManager;
import com.rudy.go4lunch.model.User;
import com.rudy.go4lunch.ui.MainActivity;
import com.rudy.go4lunch.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class NotificationsService extends FirebaseMessagingService {

    private final String NOTIFICATION_TAG = getString(R.string.app_name);
    private final MainViewModel mViewModel = new ViewModelProvider((ViewModelStoreOwner) this).get(MainViewModel.class);//todo cast ok ?
    private final UserManager userManager = UserManager.getInstance();
    private String mWorkmatesWhoJoined = "";
    private String mRestaurant;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        userManager.getUserData().addOnSuccessListener(user -> {
            if (user.getBookedRestaurantPlaceId() != null) {
                mRestaurant = user.getBookedRestaurant();
                mViewModel.getDataBaseInstanceUser();
                mViewModel.getAllUsers().observe((LifecycleOwner) this, users1 -> {//todo cast ok ?
                    for (User allUser : users1) {
                        if (Objects.equals(allUser.getBookedRestaurantPlaceId(), user.getBookedRestaurantPlaceId())) {
                            List<String> workmates = new ArrayList<>();
                            workmates.add(allUser.getUsername());
                            String joined = String.join(",", workmates);
                            joined += ".";
                            mWorkmatesWhoJoined = getString(R.string.with) + joined;
                        }
                    }
                });
            }
        });

        String statusNotification = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                .getString(SHARED_PREF_USER_INFO_NOTIFICATION, "null");
        if (statusNotification.equals(NO)) {
            //Do not receive notification
            FirebaseMessaging.getInstance().unsubscribeFromTopic("APP");
        } else if (message.getNotification() != null && mRestaurant != null) {
            RemoteMessage.Notification notification = message.getNotification();
            sendVisualNotification(notification, mWorkmatesWhoJoined, mRestaurant);
            Log.e("TAG", notification.getBody());
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private void sendVisualNotification(RemoteMessage.Notification notification, String workmates, String restaurant) {
        // Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // Create a Channel (Android 8)
        String channelId = getString(R.string.channel_id);

        // Build a Notification object
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.restaurant_logo)
                        .setContentTitle(notification.getTitle())
                        .setContentText(restaurant + workmates)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = getString(R.string.channel_name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        // Show notification
        int NOTIFICATION_ID = 7;
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }
}
