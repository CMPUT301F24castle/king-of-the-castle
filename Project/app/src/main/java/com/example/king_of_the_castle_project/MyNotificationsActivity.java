package com.example.king_of_the_castle_project;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class for viewing an entrants'  invitations
 */
public class MyNotificationsActivity extends AppCompatActivity {
    private ListView listView;
    private NotificationArrayAdapter arrayAdapter;
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
        setContentView(R.layout.entrant_view_invitations_screen);
        // Button
        Button returnButton = findViewById(R.id.return_button);
        Button moreButton = findViewById(R.id.more_button);
        // Get android id
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        // Initialize the firebase
        db = FirebaseFirestore.getInstance();
        // find listview
        listView = findViewById(R.id.notification_list);
        // Initialize the events array
        events = new ArrayList<>();
        // Adapter
        arrayAdapter = new NotificationArrayAdapter(this, events);
        listView.setAdapter(arrayAdapter);
        // Fetch events from firebase, then events.add
        db.collection("events")
                .whereArrayContains("acceptedList", androidId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        events.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // get waitlist
                            ArrayList<String> entrantIds = new ArrayList<>();
                            ArrayList<Map<String, Object>> waitlist = (ArrayList<Map<String, Object>>) document.get("waitList");
                            if (waitlist != null) {
                                for (Map<String, Object> entry : waitlist) {
                                    // Extract the "entrantID" field
                                    if (entry.containsKey("entrantID")) {
                                        entrantIds.add((String) entry.get("entrantID"));
                                    }
                                }
                            }

                            // add waitlist and other fields to Event object
                            Event event = new Event(document.getString("name"), document.getString("date"), document.getString("time"), document.getString("location"), document.getString("eventDetails"), document.getLong("maxParticipants").intValue(), entrantIds, (ArrayList<String>) document.get("acceptedList"), (ArrayList<String>) document.get("declinedList"), (ArrayList<String>) document.get("registeredList"), document.getBoolean("geolocation"), document.getString("qrCodeData"), document.getString("organizerID"));

                            events.add(event);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                });

        moreButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyLossNotificationsActivity.class);
            startActivity(intent);
        });

        // Return button
        returnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }
}
