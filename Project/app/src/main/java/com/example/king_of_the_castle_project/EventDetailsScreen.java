package com.example.king_of_the_castle_project;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import android.Manifest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Activity that shows toolbar and buttons on Event Details Screen.
 * Allows you to join a waiting list for an event
 */
public class EventDetailsScreen extends AppCompatActivity {
    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationClient;

    private TextView eventNameTV;
    private TextView eventDateTV;
    private TextView eventTimeTV;
    private TextView eventLocationTV;
    private TextView eventMaxParticipantsTV;
    private TextView eventNotesTV;
    private TextView joinWaitlistWithWarningTV;
    private TextView joinWaitlistNoWarningTV;
    private ImageView qrImage;

    private AppCompatButton yesButton;
    private AppCompatButton noButton;
    private AppCompatButton returnBut;

    private FirebaseFirestore db;
    private String entrantID;
    private String scannedResult;

    private String eventName;
    private String eventDate;
    private String eventLocation;
    private int eventMaxParticipants;
    private String eventTime;
    private String eventNotes;
    private String qrCodeString;
    private Boolean hasGeolocation;

    private Double latitude;
    private Double longitude;

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

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        eventNameTV = findViewById(R.id.event_name_TV);
        eventDateTV = findViewById(R.id.date_TV);
        eventTimeTV = findViewById(R.id.time_TV);
        eventLocationTV = findViewById(R.id.venue_TV);
        eventMaxParticipantsTV = findViewById(R.id.max_participants_TV);
        eventNotesTV = findViewById(R.id.notes_TV);
        joinWaitlistWithWarningTV = findViewById(R.id.join_waitlist_tv_with_warning);
        joinWaitlistNoWarningTV = findViewById(R.id.join_waitlist_tv_no_warning);
        qrImage = findViewById(R.id.qr_image);


        yesButton = findViewById(R.id.button_yes);
        noButton = findViewById((R.id.button_no));
        returnBut = findViewById(R.id.return_button);

        Intent intent = getIntent();
        scannedResult = intent.getStringExtra("qr result");

        // set event details values
        showDetails();

        // get user's androidID
        entrantID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get user's latitude and longitude if geolocation is set
                if (hasGeolocation) {

                    // Check for location permissions
                    if (ActivityCompat.checkSelfPermission(EventDetailsScreen.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EventDetailsScreen.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                    else {
                        // Permissions already granted, set up LocationManager
                        getLatAndLong();
                    }
                }
                else {
                    joinWaitlist();
                }
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                Intent intent = new Intent(getApplicationContext(), EntrantScreenActivity.class);
                startActivity(intent);
            }
        });

        returnBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                Intent intent = new Intent(getApplicationContext(), EntrantScreenActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Adds the entrant to the event waitlist in Firebase Firestore
     */
    private void joinWaitlist() {
        // if the event uses geolocation
        if (hasGeolocation) {
            // create entry hashmap
            Map<String, Object> waitlistEntry = new HashMap<>();
            waitlistEntry.put("entrantID", entrantID);
            waitlistEntry.put("latitude", latitude);
            waitlistEntry.put("longitude", longitude);

            db.collection("events")
                    .whereEqualTo("hashIdentifier", scannedResult)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();

                            db.collection("events").document(documentId).get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if (documentSnapshot.exists()) {
                                            ArrayList<Map<String, Object>> waitList =
                                                    (ArrayList<Map<String, Object>>) documentSnapshot.get("waitList");

                                            boolean alreadyOnWaitlist = waitList != null && waitList.stream()
                                                    .anyMatch(entry -> entrantID.equals(entry.get("entrantID")));

                                            if (alreadyOnWaitlist) {
                                                Toast.makeText(this, "You are already on the waitlist!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                db.collection("events").document(documentId)
                                                        .update("waitList", FieldValue.arrayUnion(waitlistEntry))
                                                        .addOnSuccessListener(aVoid ->
                                                                Toast.makeText(this, "You have joined the waitlist!", Toast.LENGTH_SHORT).show())
                                                        .addOnFailureListener(e ->
                                                                Toast.makeText(this, "Failed to join waitlist: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                            }
                                        }
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(this, "Error fetching event details: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        } else {
                            Toast.makeText(this, "Event not found for the scanned QR code", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed to retrieve event: " + e.getMessage(), Toast.LENGTH_SHORT).show());

            Intent intent = new Intent(getApplicationContext(), EntrantScreenActivity.class);
            startActivity(intent);

        }

        // if the event doesn't use geolocation
        else {
            db.collection("events")
                    .whereEqualTo("hashIdentifier", scannedResult)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();

                            db.collection("events").document(documentId).get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if (documentSnapshot.exists()) {
                                            List<String> waitList = (List<String>) documentSnapshot.get("waitList");

                                            if (waitList != null && waitList.contains(entrantID)) {
                                                Toast.makeText(this, "You are already on the waitlist!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                db.collection("events").document(documentId)
                                                        .update("waitList", FieldValue.arrayUnion(entrantID))
                                                        .addOnSuccessListener(aVoid ->
                                                                Toast.makeText(this, "You have joined the waitlist!", Toast.LENGTH_SHORT).show())
                                                        .addOnFailureListener(e ->
                                                                Toast.makeText(this, "Failed to join waitlist: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                            }
                                        }
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(this, "Error fetching event details: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        } else {
                            Toast.makeText(this, "Event not found for the scanned QR code", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed to retrieve event: " + e.getMessage(), Toast.LENGTH_SHORT).show());

            Intent intent = new Intent(getApplicationContext(), EntrantScreenActivity.class);
            startActivity(intent);

        }
    }

    /**
     * Uses the event name to query the database to find the all the event details and shows the
     * details on the screen
     */
    private void showDetails() {
        // fetch firebase reference
        //db = FirebaseFirestore.getInstance();

        // query the QR code
        db.collection("events")
                .whereEqualTo("hashIdentifier", scannedResult)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        boolean eventFound = false;

                        // Iterate through the results
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // get event details to show
                            eventName = document.getString("name");
                            eventDate = document.getString("date");
                            eventNotes = document.getString("details");
                            eventLocation = document.getString("location");
                            eventMaxParticipants = document.contains("maxParticipants")
                                    ? document.getLong("maxParticipants").intValue()
                                    : 0;
                            eventTime = document.getString("time");
                            qrCodeString = document.getString("qrCodeData");
                            hasGeolocation = document.getBoolean("geolocation");


                            eventNameTV.setText(eventName);
                            eventDateTV.append(eventDate);
                            eventTimeTV.append(eventTime);
                            eventLocationTV.append(eventLocation);
                            eventMaxParticipantsTV.append(String.valueOf(eventMaxParticipants));
                            eventNotesTV.append(eventNotes != null ? eventNotes : "N/A");
                            if (hasGeolocation) {
                                joinWaitlistWithWarningTV.setVisibility(View.VISIBLE);
                            }
                            else {
                                joinWaitlistNoWarningTV.setVisibility(View.VISIBLE);
                            }

                            if (qrCodeString != null && !qrCodeString.isEmpty()) {
                                try {
                                    byte[] decodedBytes = Base64.decode(qrCodeString, Base64.DEFAULT);
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                                    qrImage.setImageBitmap(bitmap);
                                } catch (IllegalArgumentException e) {
                                    Toast.makeText(this, "Invalid QR Code Image", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                qrImage.setImageResource(R.drawable.missing_image_icon); // Show a placeholder if QR code is missing
                            }

                            break; // Exit loop after processing the first matching document
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) { // Match the requestCode in requestPermissions
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with location retrieval
                getLatAndLong();
                Toast.makeText(this, "Location permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(this, "You denied geolocation. You didn't join this event's waitlist", Toast.LENGTH_LONG).show();
                Intent returnintent = new Intent(getApplicationContext(), EntrantScreenActivity.class);
                startActivity(returnintent); // Exit if permission not granted
            }
        }
    }

    private void getLatAndLong() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Check permissions again
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Log.d("Debug", "Requesting location updates...");

        // Request current location
        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.d("Debug", "Current Location: Lat=" + latitude + ", Lon=" + longitude);

                        // Add to Firebase
                        joinWaitlist();
                    } else {
                        Toast.makeText(this, "Unable to get location. Try again later.", Toast.LENGTH_SHORT).show();
                        Log.d("Debug", "Current Location is null.");
                    }
                })
                .addOnFailureListener(this, e -> {
                    Toast.makeText(this, "Failed to get location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "Error getting location", e);
                });
    }
}