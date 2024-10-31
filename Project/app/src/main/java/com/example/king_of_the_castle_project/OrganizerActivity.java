package com.example.king_of_the_castle_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class OrganizerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_dashboard);
        final Button createEventButton = findViewById(R.id.create_event_button);
        final Button manageEventButton = findViewById(R.id.manage_event_button);
        final Button manageFacilityButton = findViewById(R.id.manage_facility_button);
        final Button changeRole = findViewById(R.id.change_role_button);

        // change role should return to the choose role screen (where intent is called)
        changeRole.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        // Clicking on manage facility goes to manage facility screen/activity
        manageFacilityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(OrganizerActivity.this, ManageFacilityActivity.class);
                startActivity(intent);
            }
        });

        // onclick "Create Event" move to event creation screen
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizerActivity.this, CreateEventActivity.class);
                startActivity(intent);
            }
        });


        // copied from main activity lol
        /*
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
    }
}
