package com.example.king_of_the_castle_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Landing page for administrators
 */
public class AdministratorDashboardActivity extends AppCompatActivity {
    /**
     * Default method for basic startup logic
     * @param savedInstanceState
     *      If there was an Instance saved, saved instances restores it
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administrator_dashboard);

        final Button browseEventsButton = findViewById(R.id.browse_event_button);
        final Button browseProfilesButton = findViewById(R.id.browse_profile_button);
        final Button browseImagesButton = findViewById(R.id.browse_images_button);
        final Button changeRole = findViewById(R.id.change_role_button);

        // change role should return to the choose role screen (where intent is called)
        changeRole.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        browseEventsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AdministratorDashboardActivity.this, BrowseEventsActivity.class);
                startActivity(intent);
            }
        });

        browseProfilesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AdministratorDashboardActivity.this, BrowseProfilesActivity.class);
                startActivity(intent);
            }
        });

        browseImagesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AdministratorDashboardActivity.this, BrowseImages.class);
                startActivity(intent);
            }
        });
    }
}
