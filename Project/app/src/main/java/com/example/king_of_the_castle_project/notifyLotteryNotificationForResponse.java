package com.example.king_of_the_castle_project;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.List;

/**
 * This will send a notification to selected entrants (by lottery system) if they would like to accept or decline their invitation (we are still waiting on their response)
 * 2.7.2: As an organiser I want to send notifications to all selected entrants
 */
//2.7.2: As an organiser I want to send a notification to chosen entrants to sign up for events
public class notifyLotteryNotificationForResponse {

    private Context context;
    private static final String CHANEL_ID = "sendLotteryNotificationForResponse";

    public notifyLotteryNotificationForResponse(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "sendLotteryNotificationForResponse";
            String description = "Notifications for chosen entrants to accept or decline their invitation";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendLotteryNotificationForResponse(Lottery lottery) {
        List<String> selectedAttendees = lottery.getSelectedAttendees();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        for (String userID : selectedAttendees) {
            String message = "Congratulations. You have been chosen through the lottery! Would you like to accept or decline? (Please note we are still waiting for your response)";
            //(ADD) buttons for accept and decline

            //builds the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANEL_ID)
                    .setContentTitle("Lottery Status")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true); //dismiss notification when clicked

            // Send the notification with hashCode() as ID
            notificationManager.notify(userID.hashCode(), builder.build());
        }
    }


}
