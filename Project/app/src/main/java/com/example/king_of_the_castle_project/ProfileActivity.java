package com.example.king_of_the_castle_project;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavView;
    private TextView entrantNameTV;
    private TextView entrantEmailTV;
    private TextView entrantPhoneTV;
    private ImageView entrantPhotoIV;
    private AppCompatButton editProfileButton;

    private FirebaseFirestore db;
    private String androidID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavView = findViewById(R.id.bottom_nav);
        bottomNavView.setSelectedItemId(R.id.bottom_profile);

        entrantNameTV = findViewById(R.id.name_TV_profile_screen);
        entrantEmailTV = findViewById(R.id.email_TV_profile_screen);
        entrantPhoneTV = findViewById(R.id.phone_number_TV_profile_screen);
        entrantPhotoIV = findViewById(R.id.profile_photo_IV);
        editProfileButton = findViewById(R.id.edit_profile_button);

        db = FirebaseFirestore.getInstance();
        androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        bottomNavView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), EntrantScreenActivity.class));
                return true;

            } else if (item.getItemId() == R.id.bottom_profile) {
                return true;

            } else if (item.getItemId() == R.id.bottom_settings) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;

            } else {
                return false;
            }
        });

        showProfile();

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
            }
        });

    }

    private void showProfile() {
        db.collection("entrants")
                .whereEqualTo("id", androidID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            entrantNameTV.append(document.getString("name"));
                            entrantEmailTV.append(document.getString("email"));
                            String phone = document.getString("phone");
                            if (phone != null && !phone.isEmpty()) {
                                entrantPhoneTV.append(phone);
                            } else {
                                entrantPhoneTV.append("No phone number provided");
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}