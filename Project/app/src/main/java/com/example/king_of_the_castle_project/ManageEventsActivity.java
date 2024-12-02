package com.example.king_of_the_castle_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * Class used to manage the screen for viewing organizer's activities. Pulls from firebase
 */
public class ManageEventsActivity extends AppCompatActivity implements EventArrayAdapter.ImageSelection {
    private ListView listView;
    private EventArrayAdapter arrayAdapter;
    private ArrayList<Event> events;
    private FirebaseFirestore db;
    private String androidId;
    private String eventHashHolder;

    // Used for image selection
    private ActivityResultLauncher<Intent> imageSelectorLauncher;

    /**
     * Default method for basic startup logic
     * @param savedInstanceState
     *      If there was an Instance saved, saved instances restores it
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        arrayAdapter = new EventArrayAdapter(this, events, this);
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

        // Image selection handler
        imageSelectorLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // Get image URI from result
                        Uri imageUri = result.getData().getData();
                        // Get the image (event) ID
                        String eventHashIdentifier = eventHashHolder;
                        if (imageUri != null) {
                            try {
                                // fetch image
                                Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                                // convert to base64 for firebase
                                String convertedImage = convertImageToBase64(image);
                                // firebase stuff
                                db.collection("images")
                                        .document(eventHashIdentifier)
                                        .get()
                                        .addOnSuccessListener(documentSnapshot -> {
                                            if (documentSnapshot.exists()) {
                                                // check for existing image
                                                db.collection("images")
                                                        .document(eventHashIdentifier)
                                                        .update("imageData", convertedImage);
                                            } else {
                                                db.collection("images")
                                                        .document(eventHashIdentifier)
                                                        .set(Map.of(
                                                                "imageData", convertedImage,
                                                                "eventIdentifier", eventHashIdentifier
                                                        ));
                                            }
                                        })
                                        .addOnCompleteListener(task -> {
                                            eventHashHolder = null; // Clear after operation
                                        })
                                        .addOnFailureListener(task -> {
                                            eventHashHolder = null; // Clear after operation
                                        });
                                Toast.makeText(this, "Image Successfully Edited", Toast.LENGTH_LONG).show();
                                finish();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            Toast.makeText(this, "No image selected", Toast.LENGTH_LONG).show();
                            finish();
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

    /**
     * Function that converts images (in bitmap format) to base64 strings.
     * @param bitmap
     *      The bitmap that needs to be converted
     * @return
     *      The base64 conversion for the bitmap
     */
    private String convertImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    /**
     * Implementation of image button interface
     * @param eventHashIdentifier
     *      Identifier for the event that needs to be updated
     */
    @Override
    public void onImageSelectionRequested(String eventHashIdentifier) {
        // Hold event hash
        eventHashHolder = eventHashIdentifier;
        // Launch the image selection intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imageSelectorLauncher.launch(intent); // Launch the image picker
    }
}
