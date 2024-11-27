package com.example.king_of_the_castle_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * Class used to manage the screen for viewing organizer's activities. Pulls from firebase
 */
public class ManageEventsActivity extends AppCompatActivity {
    private ListView listView;
    private EventArrayAdapter arrayAdapter;
    private ArrayList<Event> events;
    private FirebaseFirestore db;
    private String androidId;

    /**
     * Default method for basic startup logic
     * @param savedInstanceState
     *      If there was an Instance saved, saved instances restores it
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this);
        setContentView(R.layout.organizer_manage_events_screen);
        // Button
        Button returnButton = findViewById(R.id.return_button);
        // Get android id
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        // Initialize the firebase
        db = FirebaseFirestore.getInstance();
        // find listview
        listView = findViewById(R.id.event_list);
        // Initialize the events array
        events = new ArrayList<>();
        // Adapter
        arrayAdapter = new EventArrayAdapter(this, events);
        listView.setAdapter(arrayAdapter);
        // Fetch events from firebase, then events.add
        db.collection("events")
                .whereEqualTo("organizerID", androidId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Check if the query returned any results
                        if (task.getResult() != null && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Parse the document

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

                                // Add the event to the events list
                                events.add(event);
                            }
                            // Notify the adapter to refresh the ListView
                            arrayAdapter.notifyDataSetChanged();
                        } else {
                            // No events found for the organizer
                            Toast.makeText(this, "No events found for this organizer", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Task failed, show an error message
                        Toast.makeText(this, "Failed to retrieve events: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("FirestoreError", "Error fetching events", task.getException());
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any failure of the Firestore query
                    Toast.makeText(this, "Failed to retrieve events: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreError", "Error fetching events", e);
                });

        // Return button
        returnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        /*
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
    }
}
