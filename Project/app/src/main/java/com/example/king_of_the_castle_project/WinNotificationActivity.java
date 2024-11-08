package com.example.king_of_the_castle_project;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * Class that is an activity for when a user wins the lottery for an event
 */
public class WinNotificationActivity extends AppCompatActivity {
    private Toast toastMessage;
    private String qrCode;
    private FirebaseFirestore db;
    private String entrantId;

    /**
     * Creates the activity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrant_win_notification_activity);

        Button acceptButton = findViewById(R.id.accept_invite_button);
        Button declineButton = findViewById(R.id.decline_invite_button);

        db = FirebaseFirestore.getInstance();
        entrantId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        qrCode = getIntent().getStringExtra("event");

        acceptButton.setOnClickListener(v -> {
            toastMessage = Toast.makeText(this, "You have accepted the invitation", Toast.LENGTH_SHORT);
            toastMessage.show();
            // Add code adding entrant to registered list

            finish();
        });

        declineButton.setOnClickListener(v -> {
            toastMessage = Toast.makeText(this, "You have declined the invitation", Toast.LENGTH_SHORT);
            toastMessage.show();
            // Add code adding entrant to declined list
            finish();

        });
    }

    private void acceptInvite() {
        db.collection("events")
                .whereEqualTo("qrCodeData", qrCode)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();

                        db.collection("events").document(documentId)
                                .update("registeredList", FieldValue.arrayUnion(entrantId))
                                //});.update("registeredList", FieldValue.arrayUnion(entrantId))
                                .addOnSuccessListener(aVoid ->
                                        Toast.makeText(this, "You have accepted the invitation", Toast.LENGTH_SHORT).show())

                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Failed to accept the invitation: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Event not found for the invitation", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to retrieve event: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

}
