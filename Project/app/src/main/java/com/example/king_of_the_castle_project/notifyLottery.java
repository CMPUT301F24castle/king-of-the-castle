package com.example.king_of_the_castle_project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import java.util.List;




/**
 * This will send a notification to all chosen entrants (from lottery class) asking if they would like to accept or decline their invitation
 * 2.5.1: As an organiser I want to send a notification to chosen entrants to sign up for events
 */
//2.5.1: As an organiser I want to send a notification to chosen entrants to sign up for events
public class notifyLottery implements View.OnClickListener {
    //private Context context;
    private static final String CHANEL_ID = "sendLotteryNotification";
    private Lottery lottery;
    public notifyLottery(Lottery lottery) {
        this.lottery = lottery;

        //this.context = context;
       // createNotificationChannel();
    }
    @Override
    public void onClick(View view) {
        Context context = view.getContext();
        createNotificationChannel(context);

        sendLotteryNotification(context, lottery);
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "sendLotteryNotification";
            String description = "Notifications for lottery selection status";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public void sendLotteryNotification(Context context, Lottery lottery) {
        List<String> selectedAttendees = lottery.getSelectedAttendees();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isNotificationsEnabled = sharedPreferences.getBoolean("notifications_enabled", true);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (!isNotificationsEnabled) {
            return;
        }
        for (String userID : selectedAttendees) {
            String message = "Congratulations. You have been chosen through the lottery! Would you like to accept or decline?";
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
