package com.example.king_of_the_castle_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateEventActivity extends AppCompatActivity {
    private boolean geolocation_check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_creation_screen);
        // Get all event details
        EditText eventName = findViewById(R.id.event_name_edit_text);
        EditText eventDate = findViewById(R.id.event_date_edit_text);
        EditText eventLocation = findViewById(R.id.event_location_edit_text);
        EditText eventDetails = findViewById(R.id.event_details_edit_text);
        EditText eventMaxParticipants = findViewById(R.id.event_max_participants_edit_text);
        // however to check photos

        // Geolocation checkbox
        CheckBox eventGeolocation = findViewById(R.id.event_geolocation_checkbox);

        // "Create Event" button
        Button eventCreation = findViewById(R.id.create_event_button);

        // update geolocation bool whenever applicable
        eventGeolocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            geolocation_check = isChecked;
        });

        // event listener for "create event"
        eventCreation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send information to firebase first
                // TODO
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
}