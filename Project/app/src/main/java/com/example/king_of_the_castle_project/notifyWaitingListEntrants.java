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
 * This will send a notification to all entrants on the waiting list, whether they have been chosen or not
 * 2.7.1: As an organiser I want to send notifications to all entrants on the waiting list
 */
public class notifyWaitingListEntrants {
    private Context context;
    private static final String CHANEL_ID = "notifyWaitingListEntrants";

    public notifyWaitingListEntrants(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    /**
     * Creates notification channel
     */
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notifyWaitingListEntrants";
            String description = "Notifications for all entrants on waiting list";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Notifies entrants on a waiting list
     * @param event
     *  Event whose waiting list's entrants are being notified
     */
    public void notifyWaitingListEntrants(Event event) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isNotificationsEnabled = sharedPreferences.getBoolean("notifications_enabled", true);

        List<String> waitingList = event.getWaitList();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (!isNotificationsEnabled) {
            return; // Don't send the notification
        }
        for (String userID : waitingList) {
            //(ADD) the message will be a text that the organizer will input
            String message = "notification description";

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
