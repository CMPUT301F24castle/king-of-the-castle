package com.example.king_of_the_castle_project;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Class that is an activity for when a user wins the lottery for an event
 */
public class WinNotificationActivity extends AppCompatActivity {
    private Toast toastMessage;
    //private String qrCode;
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

        TextView dateText = findViewById(R.id.date_textview);
        TextView timeText = findViewById(R.id.time_textview);
        TextView nameText = findViewById(R.id.name_textview);
        TextView locationText = findViewById(R.id.venue_textview);
        TextView notesText = findViewById(R.id.time_textview);
        TextView maxText = findViewById(R.id.max_participants_textview);
        Button acceptButton = findViewById(R.id.accept_invite_button);
        Button declineButton = findViewById(R.id.decline_invite_button);

        db = FirebaseFirestore.getInstance();
        entrantId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        String qrCode = getIntent().getStringExtra("event");
        dateText.setText(getIntent().getStringExtra("date"));
        timeText.setText(getIntent().getStringExtra("time"));
        String name = getIntent().getStringExtra("name");
        nameText.setText(name);
        locationText.setText(getIntent().getStringExtra("location"));
        notesText.setText(getIntent().getStringExtra("notes"));
        maxText.setText(String.valueOf(getIntent().getIntExtra("max", 5000)));



        acceptButton.setOnClickListener(v -> {
            //toastMessage = Toast.makeText(this, "You have accepted the invitation", Toast.LENGTH_SHORT);
            //toastMessage.show();
            // Add code adding entrant to registered list
            acceptInvite(name);
            
            finish();
        });

        declineButton.setOnClickListener(v -> {
            declineInvite(name);
            // Add code adding entrant to declined list
            finish();

        });
    }

    /**
     * Allows the user to accept the invitation
     * @param name
     *  Name of the event
     */
    private void acceptInvite(String name) {
        db.collection("events")
                .whereEqualTo("name", name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                db.collection("events").document(id).update("registeredList", FieldValue.arrayUnion(entrantId));
                                db.collection("events").document(id).update("acceptedList", FieldValue.arrayRemove(entrantId));
                            }
                        }
                    }
                });
        toastMessage = Toast.makeText(this, "You have accepted the invitation", Toast.LENGTH_SHORT);
        toastMessage.show();
    }

    /**
     * Allows the user to decline an invitation
     * @param name
     */
    private void declineInvite(String name) {
        db.collection("events")
                .whereEqualTo("name", name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                db.collection("events").document(id).update("declinedList", FieldValue.arrayUnion(entrantId));
                                db.collection("events").document(id).update("acceptedList", FieldValue.arrayRemove(entrantId));
                            }
                        }
                    }
                });
        toastMessage = Toast.makeText(this, "You have declined the invitation", Toast.LENGTH_SHORT);
        toastMessage.show();
    }

}
