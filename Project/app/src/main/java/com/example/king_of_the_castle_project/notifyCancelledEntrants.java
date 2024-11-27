package com.example.king_of_the_castle_project;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import java.util.List;

/**
 * This sends a notification to all cancelled entrants
 * 2.7.3: As an organiser I want to send a notification to all cancelled entrants
 */
//2.7.3: As an organiser I want to send a notification to all cancelled entrants
public class notifyCancelledEntrants {
    private Context context;
    private static final String CHANEL_ID = "notifyCancelledEntrants";

    public notifyCancelledEntrants(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notifyCancelledEntrants";
            String description = "Notifications for all enntrants who cancelled their invitation";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void notifyCancelledEntrants(Event event) {

        List<String> cancelledList = event.getDeclinedList();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isNotificationsEnabled = sharedPreferences.getBoolean("notifications_enabled", true);

        if (!isNotificationsEnabled) {
            return; // Don't send the notification
        }
        for (String userID : cancelledList) {
            String message = "notification description";
            //(ADD) the message will be a text that the organizer inputs


            //builds the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANEL_ID)
                    .setContentTitle("notification title")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true); //dismiss notification when clicked

            // Send the notification with hashCode() as ID
            notificationManager.notify(userID.hashCode(), builder.build());
        }
    }




}
