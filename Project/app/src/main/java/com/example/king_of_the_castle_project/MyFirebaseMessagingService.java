package com.example.king_of_the_castle_project;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d("FCM", "New token: " + token);

        // Send token to Firestore
        sendTokenToFirestore(token);
    }

    private void sendTokenToFirestore(String token) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = "exampleUserId"; // Replace with the actual user ID

        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("fcmToken", token);

        db.collection("entrants").document(userId)
                .set(tokenMap)
                .addOnSuccessListener(aVoid -> Log.d("FCM", "Token saved successfully"))
                .addOnFailureListener(e -> Log.e("FCM", "Error saving token", e));
    }







}
