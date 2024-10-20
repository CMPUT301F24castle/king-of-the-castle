package com.example.king_of_the_castle_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Activity that shows toolbar and buttons on Event Details Screen.
 * Allows you to join a waiting list for an event
 */
public class EventDetailsScreen extends AppCompatActivity {

    private TextView eventDetails;
    private AppCompatButton yesButton;
    private AppCompatButton noButton;
    private AppCompatButton returnBut;

    /**
     * Default method that performs basic application startup logic
     * @param savedInstanceState
     *          If there was an Instance saved, saved instances restores it
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details_screen);

        eventDetails = findViewById(R.id.event_details);
        yesButton = findViewById(R.id.button_yes);
        noButton = findViewById((R.id.button_no));
        returnBut = findViewById(R.id.retun_button);

        Intent intent = getIntent();
        String scannedResult = intent.getStringExtra("qr_result");
        eventDetails.setText(scannedResult);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
}