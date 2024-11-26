package com.example.king_of_the_castle_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsActivity extends AppCompatActivity {
    private SwitchCompat geolocationSwitch;
    private SwitchCompat notificationSwitch;
    private BottomNavigationView bottomNavView;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        geolocationSwitch = findViewById(R.id.switch_geolocation);
        notificationSwitch = findViewById(R.id.switch_notifications);

        bottomNavView = findViewById(R.id.bottom_nav);
        bottomNavView.setSelectedItemId(R.id.bottom_settings);

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