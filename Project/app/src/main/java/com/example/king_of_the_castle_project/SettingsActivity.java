package com.example.king_of_the_castle_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Activity that allows user to change geolocation and notification settings
 */
public class SettingsActivity extends AppCompatActivity {
    private SwitchCompat geolocationSwitch;
    private SwitchCompat notificationSwitch;
    private static final String PREFS_NAME = "user_preferences";
    private static final String KEY_NOTIFICATIONS = "notifications_enabled";
    private BottomNavigationView bottomNavView;

    private FirebaseFirestore db;

    /**
     * Default method that performs basic application startup logic
     * @param savedInstanceState
     *          If there was an Instance saved, saved instances restores it
     *
     * @see ProfileActivity
     * @see EntrantScreenActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // get views
        geolocationSwitch = findViewById(R.id.switch_geolocation);
        notificationSwitch = findViewById(R.id.switch_notifications);
        // Load saved notifications preference
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isNotificationsEnabled = sharedPreferences.getBoolean(KEY_NOTIFICATIONS, false);  // Default to false
        notificationSwitch.setChecked(isNotificationsEnabled);
        // Save notifications preference when Switch is toggled
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(KEY_NOTIFICATIONS, isChecked);
            editor.apply();
        });



        bottomNavView = findViewById(R.id.bottom_nav);
        bottomNavView.setSelectedItemId(R.id.bottom_settings);

        // set up bottom navigation between home, profile and settings
        bottomNavView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), EntrantScreenActivity.class));
                return true;

            } else if (item.getItemId() == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                return true;

            } else if (item.getItemId() == R.id.bottom_settings) {
                return true;

            } else {
                return false;
            }
        });



    }
}