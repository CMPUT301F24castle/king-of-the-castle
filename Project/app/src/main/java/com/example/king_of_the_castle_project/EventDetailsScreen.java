package com.example.king_of_the_castle_project;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Activity that shows toolbar and buttons on Event Details Screen.
 * Allows you to join a waiting list for an event
 */
public class EventDetailsScreen extends AppCompatActivity {

    private TextView eventDetails;
    private AppCompatButton yesButton;
    private AppCompatButton noButton;
    private AppCompatButton returnBut;

    private FirebaseFirestore db;
    private String entrantID;
    private String scannedResult;

    /**
     * Default method that performs basic application startup logic
     * @param savedInstanceState
     *          If there was an Instance saved, saved instances restores it
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details_screen);

        db = FirebaseFirestore.getInstance();

        eventDetails = findViewById(R.id.event_details);
        yesButton = findViewById(R.id.button_yes);
        noButton = findViewById((R.id.button_no));
        returnBut = findViewById(R.id.retun_button);

        Intent intent = getIntent();
        scannedResult = intent.getStringExtra("qr_result");
        eventDetails.setText(scannedResult);

        entrantID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinWaitlist();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EntrantScreenActivity.class);
                startActivity(intent);
            }
        });

        returnBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EntrantScreenActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Adds the entrant to the event waitlist in Firebase Firestore
     */
    private void joinWaitlist() {
        db.collection("events").document(scannedResult)
                .update("waitlist", FieldValue.arrayUnion(entrantID))
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(this, "You have joined the waitlist!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to join waitlist: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}