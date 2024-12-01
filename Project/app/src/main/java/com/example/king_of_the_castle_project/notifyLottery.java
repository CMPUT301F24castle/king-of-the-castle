package com.example.king_of_the_castle_project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.List;




/**
 * This will send a notification to all chosen entrants (from lottery class) asking if they would like to accept or decline their invitation
 * 2.5.1: As an organiser I want to send a notification to chosen entrants to sign up for events
 */
//2.5.1: As an organiser I want to send a notification to chosen entrants to sign up for events
public class notifyLottery implements View.OnClickListener {
    //private Context context;
   // private static final String CHANNEL_ID = "sendLotteryNotification";
    private static final String CHANNEL_ID = "sendLotteryNotification";
    //private Lottery lottery;
    private final String eventId;
    private final FirebaseFirestore db;

    public notifyLottery(String eventId) {
       // this.lottery = lottery;
        this.eventId = eventId;
        this.db = FirebaseFirestore.getInstance();

        //this.context = context;
       // createNotificationChannel();
    }
    @Override
    public void onClick(View view) {
        Context context = view.getContext();
        createNotificationChannel(context);

        //sendLotteryNotification(context, lottery);
        fetchAcceptedListAndSendNotifications(context);
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "sendLotteryNotification";
            String description = "Notifications for lottery selection status";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


  /*  public void sendLotteryNotification(Context context, Lottery lottery) {
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
                    .setSmallIcon(R.drawable.baseline_adb_24)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true); //dismiss notification when clicked

            // Send the notification with hashCode() as ID
            notificationManager.notify(userID.hashCode(), builder.build());
        } */

    private void fetchAcceptedListAndSendNotifications(Context context) {
        db.collection("events").document(eventId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> acceptedList = (List<String>) documentSnapshot.get("acceptedList");
                        if (acceptedList != null && !acceptedList.isEmpty()) {
                            Log.d("notif", acceptedList.toString());
                            sendNotificationsToAcceptedList(context, acceptedList);
                        } else {
                            Log.d("NotifyLottery", "No accepted list found for event: " + eventId);
                        }
                    } else {
                        Log.d("NotifyLottery", "Event not found: " + eventId);
                    }
                })
                .addOnFailureListener(e -> Log.e("NotifyLottery", "Failed to fetch accepted list: ", e));
    }

   /* private void sendNotificationsToAcceptedList(Context context, List<String> acceptedList) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isNotificationsEnabled = sharedPreferences.getBoolean("notifications_enabled", true);

        if (!isNotificationsEnabled) {
            return;
        }


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        for (Object item : acceptedList) {
            String userId = null;
            if (item instanceof String) {
                userId = (String) item;
                Log.d("user", "user id is " + userId);
                String message = "You have been selected from the waiting list! Please confirm your participation.";
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentTitle("Event Lottery")
                        .setSmallIcon(R.drawable.baseline_adb_24) // Replace with your actual icon
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

                if (notificationManager != null) {
                    notificationManager.notify(userId.hashCode(), builder.build());
                }
            } else {
                Log.d("NotifyLottery", "No hashIdentifier found for item: " + item);
            }
        }
    } */


    private void sendNotificationsToAcceptedList(Context context, List<String> acceptedList) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isNotificationsEnabled = sharedPreferences.getBoolean("notifications_enabled", true);

        if (!isNotificationsEnabled) {
            return;
        }

        for (String userId : acceptedList) {
            // Fetch the FCM token for the user
            db.collection("entrants").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String fcmToken = documentSnapshot.getString("fcmToken");
                            if (fcmToken != null) {
                                Log.d("FCM", "Sending notification to token: " + fcmToken);
                                sendPushNotification(fcmToken, "You have been selected from the waiting list! Please confirm your participation.");
                            } else {
                                Log.d("FCM", "No FCM token found for user: " + userId);
                            }
                        } else {
                            Log.d("FCM", "No entrant document found for user: " + userId);
                        }
                    })
                    .addOnFailureListener(e -> Log.e("FCM", "Error fetching FCM token for user: " + userId, e));
        }
    }

    private void sendPushNotification(String fcmToken, String message) {
        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(fcmToken + "@fcm.googleapis.com")
                .setMessageId(Integer.toString((int) System.currentTimeMillis()))
                .addData("title", "Event Lottery")
                .addData("message", message)
                .build());

        Log.d("FCM", "Push notification sent to token: " + fcmToken);
    }


}













