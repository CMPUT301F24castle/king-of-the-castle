package com.example.king_of_the_castle_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class OrganizerActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> activityResultLauncher;

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

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent returnData = result.getData();
                    if (returnData != null) {
                        // String facilityName = returnData.getStringExtra("facilityModified");
                        if (returnData.hasExtra("facilityModified")) { // mod facility notification
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.toast_notification_layout, findViewById(R.id.custom_toast_container));
                            TextView text = layout.findViewById(R.id.toast_text);
                            text.setText("Facility has been successfully modified");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.setGravity(Gravity.BOTTOM, 0, 100);
                            toast.show();
                            // Make sure it doesn't run again
                            returnData.removeExtra("facilityModified");
                        } else if (returnData.hasExtra("eventCreated")) { // Create event notification
                            String eventName = returnData.getStringExtra("eventCreated");
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.toast_notification_layout, findViewById(R.id.custom_toast_container));
                            TextView text = layout.findViewById(R.id.toast_text);
                            text.setText(String.format("Event %s has been successfully created", eventName));
                            Toast toast = new Toast(getApplicationContext());
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.setGravity(Gravity.BOTTOM, 0, 100);
                            toast.show();
                            // Make sure it doesn't run again
                            returnData.removeExtra("eventCreated");
                        }
                        // for create event notification
                    }
                }
        );

        // Clicking on manage facility goes to manage facility screen/activity
        manageFacilityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(OrganizerActivity.this, ManageFacilityActivity.class);
                activityResultLauncher.launch(intent);
            }
        });

        // onclick "Create Event" move to event creation screen
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizerActivity.this, CreateEventActivity.class);
                activityResultLauncher.launch(intent);
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
