package com.example.king_of_the_castle_project;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * Specific activity for when the user is looking at their cancelled/declined events
 */
public class MyLossNotificationsActivity extends AppCompatActivity {
    private ListView listView;
    private NotificationLossArrayAdapter arrayAdapter;
    private ArrayList<Event> events;
    private FirebaseFirestore db;
    private String androidId;

    /**
     * On creation of an instance
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrant_view_losses_screen);
        // Button
        Button returnButton = findViewById(R.id.return_button);

        // Get android id
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        // Initialize the firebase
        db = FirebaseFirestore.getInstance();
        // find listview
        listView = findViewById(R.id.notification_list);
        // Initialize the events array
        events = new ArrayList<>();
        // Adapter
        arrayAdapter = new NotificationLossArrayAdapter(this, events);
        listView.setAdapter(arrayAdapter);
        // Fetch events from firebase, then events.add
        db.collection("events")
                .whereArrayContains("declinedList", androidId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Event event = document.toObject(Event.class);
                            events.add(event);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                });

        // Return button
        returnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }
}
