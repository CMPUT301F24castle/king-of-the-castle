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
    private String scannedResult = "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 6CVDpmiHrPjus+8DnvgNLp15yAfcZ8h29GV/g24AAFECABAlAABRAgAQJQAAUQIAECUAAFECABAl AABRAgAQJQAAUQIAECUAAFECABBlDpplQ4Z5lwzZYT5xTHjIRveJj24+NwCAKAEAiBIAgCgBAIgS AIAoAQCIEgCAKAEAiBIAgCgBAIgSAIAoAQCIEgCAKAEAiDIHzV4nrvheP4B84t71vl3xE1/Rd3ED AIgSAIAoAQCIEgCAKAEAiBIAgCgBAIgSAIAoAQCIEgCAKAEAiBIAgCgBAIgSAIAoc9A/Uh6M/duJ w7z7No1PXEve9wGXDDlGmRsAQJQAAEQJAECUAABECQBAlAAARAkAQJQAAEQJAECUAABECQBAlAAA RAkAQJQAAER156Ctyw5keXigE8exl5x45ndxAwCIEgCAKAEAiBIAgCgBAIgSAIAoAQCIEgCAKAEA iBIAgCgBAIgSAIAoAQCIEgCAqMdl66YA/JAbAECUAABECQBAlAAARAkAQJQAAEQJAECUAABECQBA lAAARAkAQJQAAEQJAECUAABECQBAlAAARAkAQJQAAEQJAECUAABECQBAlAAARAkAQJQAAEQJAECU AABECQBAlAAARAkAQJQAAEQJAECUAABECQBAlAAARAkAQJQAAEQJAECUAABECQBAlAAARAkAQJQA AEQJAECUAABECQBAlAAARAkAQJQAAEQJAECUAABECQBAlAAARAkAQJQAAEQJAECUAABECQBAlAAA RAkAQJQAAEQJAECUAABECQBAlAAARAkAQJQAAEQJAECUAABE/QuUtpjyKX0YSwAAAABJRU5ErkJg gg== ";

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
        if (scannedResult == null || scannedResult.isEmpty()) {
            // Use the hardcoded value if "qr_result" is not provided in the Intent
            scannedResult = "iVBORw0KGgoAAAANSUhEUgAAAgAAAAIACAIAAAB7GkOtAAAAAXNSR0IArs4c6QAAAANzQklUBQYFMwuNgAAACT9JREFUeJzt3MFqYzkURdFyk...";
        }
        eventDetails.setText(scannedResult);

        entrantID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinWaitlist();
                Intent intent = new Intent(EventDetailsScreen.this, MyWaitlistsActivity.class);
                startActivity(intent);
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
        db.collection("events")
                .whereEqualTo("name", "Anxiety")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();

                        db.collection("events").document(documentId)
                                .update("waitList", FieldValue.arrayUnion(entrantID))
                                .addOnSuccessListener(aVoid ->
                                        Toast.makeText(this, "You have joined the waitlist!", Toast.LENGTH_SHORT).show())

                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Failed to join waitlist: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Event not found for the scanned QR code", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to retrieve event: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}