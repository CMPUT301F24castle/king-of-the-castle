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
 * This sends a notification to all cancelled entrants
 * 2.7.3: As an organiser I want to send a notification to all cancelled entrants
 */
//2.7.3: As an organiser I want to send a notification to all cancelled entrants
public class notifyCancelledEntrants implements View.OnClickListener {
    private Context context;
    private static final String CHANEL_ID = "notifyCancelledEntrants";

    private final String eventId;
    private final FirebaseFirestore db;

    public notifyCancelledEntrants(String eventId) {
        this.eventId = eventId;
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View view) {
        Context context = view.getContext();
        createNotificationChannel(context);

        //sendLotteryNotification(context, lottery);
        fetchDeclinedListAndSendNotifications(context);
    }



    private void createNotificationChannel(Context context) {
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

    public void fetchDeclinedListAndSendNotifications(Context context) {

        db.collection("events").document(eventId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> declinedList = (List<String>) documentSnapshot.get("declinedList");
                        if (declinedList != null && !declinedList.isEmpty()) {
                            Log.d("notif", declinedList.toString());
                            sendNotificationsToDeclinedList(context, declinedList);
                        } else {
                            Log.d("Notify cancelled entrants", "No declined list found for event: " + eventId);
                        }
                    } else {
                        Log.d("Notify cancelled entrants", "Event not found: " + eventId);
                    }
                })
                .addOnFailureListener(e -> Log.e("notify cancelled entrants", "Failed to fetch declined list: ", e));
    }

    private void sendNotificationsToDeclinedList(Context context, List<String> declinedList) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isNotificationsEnabled = sharedPreferences.getBoolean("notifications_enabled", true);

        if (!isNotificationsEnabled) {
            return;
        }

        for (String userId : declinedList) {
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





