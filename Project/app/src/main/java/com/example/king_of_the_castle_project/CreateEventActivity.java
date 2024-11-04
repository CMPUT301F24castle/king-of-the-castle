package com.example.king_of_the_castle_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

public class CreateEventActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> imageSelectorLauncher;
    protected boolean geolocation_check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_create_screen);
        // Get all event details
        EditText eventName = findViewById(R.id.event_name_edit_text);
        EditText eventDate = findViewById(R.id.event_date_edit_text);
        EditText eventLocation = findViewById(R.id.event_location_edit_text);
        EditText eventDetails = findViewById(R.id.event_details_edit_text);
        EditText eventMaxParticipants = findViewById(R.id.event_max_participants_edit_text);
        // however to check photos
        Button uploadImageButton = findViewById(R.id.upload_photo_button);
        Button returnButton = findViewById(R.id.return_button);
        // Geolocation checkbox
        CheckBox eventGeolocation = findViewById(R.id.event_geolocation_checkbox);

        // "Create Event" button
        Button eventCreation = findViewById(R.id.create_event_button);

        // update geolocation bool whenever applicable
        eventGeolocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            geolocation_check = isChecked;
        });

        // Image information retriever
        imageSelectorLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                    }
                }
        );

        // event listener for selecting a photo
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Open image selector
                imageSelector();
            }
        });

        // event listener for "create event"
        eventCreation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // fetch info
                String name = eventName.getText().toString();
                String location = eventLocation.getText().toString();
                String details = eventDetails.getText().toString();
                String maxParticipants = eventMaxParticipants.getText().toString();
                String date = eventDate.getText().toString();
                String time = "12:00";
                int number = 60; // Max participants for now
                // Conversion of maxparticipants to int
                try {
                    number = Integer.parseInt(maxParticipants);
                } catch (NumberFormatException e) {
                    // Throw toast dialogue, return
                    displayToastNotification("Invalid max participants, returning to organizer screen.");
                    finish();
                }
                // Create QR code
                // generateQRCode();
                // Create empty waitlist
                WaitList waitlist = new WaitList(number);
                // Create event then send to firebase
                Event newEvent = new Event(name, date, time, location, details, number, waitlist, geolocation_check);
                // Passing data back
                Intent resultIntent = new Intent();
                resultIntent.putExtra("eventCreated", name);
                setResult(RESULT_OK, resultIntent);
                // return
                finish();
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // return
                finish();
            }
        });

        // From main lol
        /*
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
    }

    protected void imageSelector() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        imageSelectorLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    protected void displayToastNotification(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_notification_layout, findViewById(R.id.custom_toast_container));
        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(message);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
    }

}