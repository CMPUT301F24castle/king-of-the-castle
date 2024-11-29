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
import java.util.Map;

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
                        // Check if the query returned any results
                        if (task.getResult() != null && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Parse the document
                                if (document.getBoolean("geolocation")) {
                                    // get waitlist
                                    ArrayList<String> entrantIds = new ArrayList<>();
                                    ArrayList<Map<String, Object>> waitlist = (ArrayList<Map<String, Object>>) document.get("waitList");
                                    if (!waitlist.isEmpty()) {
                                        for (Map<String, Object> entry : waitlist) {
                                            // Extract the "entrantID" field
                                            if (entry.containsKey("entrantID")) {
                                                entrantIds.add((String) entry.get("entrantID"));
                                            }
                                        }
                                    }
                                    // add waitlist and other fields to Event object
                                    Event event = new Event(document.getString("name"), document.getString("date"), document.getString("time"), document.getString("location"),
                                            document.getString("details"), document.getLong("maxParticipants").intValue(), entrantIds, (ArrayList<String>) document.get("acceptedList"),
                                            (ArrayList<String>) document.get("declinedList"), (ArrayList<String>) document.get("registeredList"), document.getBoolean("geolocation"),
                                            document.getString("qrCodeData"), document.getString("organizerID"), document.getString("hashIdentifier"));

                                    // Add the event to the events list
                                    events.add(event);
                                }

                                else {
                                    Event event = new Event(document.getString("name"), document.getString("date"), document.getString("time"), document.getString("location"),
                                            document.getString("details"), document.getLong("maxParticipants").intValue(), (ArrayList<String>) document.get("waitList"),
                                            (ArrayList<String>) document.get("acceptedList"), (ArrayList<String>) document.get("declinedList"), (ArrayList<String>) document.get("registeredList"),
                                            document.getBoolean("geolocation"), document.getString("qrCodeData"), document.getString("organizerID"),
                                            document.getString("hashIdentifier"));

                                    // Add the event to the events list
                                    events.add(event);
                                }
                            }
                            // Notify the adapter to refresh the ListView
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