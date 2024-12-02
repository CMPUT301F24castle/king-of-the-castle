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

import java.util.List;

/**
 * This will send a notification to all entrants on the waiting list, whether they have been chosen or not
 * 2.7.1: As an organiser I want to send notifications to all entrants on the waiting list
 */
//2.7.1: As an organiser I want to send notifications to all entrants on the waiting list
public class notifyWaitingListEntrants implements View.OnClickListener {

    private static final String CHANEL_ID = "notifyWaitingListEntrants";
    private final String eventId;
    private final FirebaseFirestore db;

    public notifyWaitingListEntrants(String eventId) {
        this.eventId = eventId;
        this.db = FirebaseFirestore.getInstance();;
    }

    @Override
    public void onClick(View view) {
        Context context = view.getContext();
        createNotificationChannel(context);

        //sendLotteryNotification(context, lottery);
        fetchWaitingListAndSendNotifications(context);
    }





    private void createNotificationChannel(Context context) {
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

    private void fetchWaitingListAndSendNotifications(Context context) {
        db.collection("events").document(eventId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> waitingList = (List<String>) documentSnapshot.get("waitingList");
                        if (waitingList != null && !waitingList.isEmpty()) {
                            Log.d("notif", waitingList.toString());
                            sendNotificationsToWaitingList(context, waitingList);
                        } else {
                            Log.d("Notify waiting list", "No waiting list found for event: " + eventId);
                        }
                    } else {
                        Log.d("notify waiting list", "waiting list not found: " + eventId);
                    }
                })
                .addOnFailureListener(e -> Log.e("notify waiting list", "Failed to fetch waiting list: ", e));
    }


    private void sendNotificationsToWaitingList(Context context, List<String> waitingList) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isNotificationsEnabled = sharedPreferences.getBoolean("notifications_enabled", true);

        if (!isNotificationsEnabled) {
            return;
        }

        for (String userId : waitingList) {
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
