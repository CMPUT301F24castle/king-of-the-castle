package com.example.king_of_the_castle_project;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

/*
 * Administrator event browser handler
 */
public class BrowseEventsActivity extends AppCompatActivity {
    /**
     * Default method for basic startup logic
     * @param savedInstanceState
     *      If there was an Instance saved, saved instances restores it
     */
    private ListView listView;
    private EventAdminArrayAdapter arrayAdapter;
    private ArrayList<Event> events;
    private FirebaseFirestore db;

    /**
     * Default method for basic startup logic
     * @param savedInstanceState
     *      If there was an Instance saved, saved instances restores it
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this);
        setContentView(R.layout.administrator_manage_events_screen);
        // Button
        Button returnButton = findViewById(R.id.return_button);
        // Initialize the firebase
        db = FirebaseFirestore.getInstance();
        // find listview
        listView = findViewById(R.id.event_list);
        // Initialize the events array
        events = new ArrayList<>();
        // Adapter
        arrayAdapter = new EventAdminArrayAdapter(this, events);
        listView.setAdapter(arrayAdapter);
        // Fetch events from firebase, then events.add
        db.collection("events")
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