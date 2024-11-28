package com.example.king_of_the_castle_project;

import android.content.Intent;
import android.graphics.Color;
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

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

/**
 *  Activity to allow an entrant to view the current values of their profile (photo, name, email, phone number)
 */
public class ProfileActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavView;
    private TextView entrantNameTV;
    private TextView entrantEmailTV;
    private TextView entrantPhoneTV;
    private ImageView entrantPhotoIV;
    private AppCompatButton editProfileButton;

    private FirebaseFirestore db;
    private String androidID;

    /**
     * Default method that performs basic application startup logic
     * @param savedInstanceState
     *          If there was an Instance saved, saved instances restores it
     *
     * @see EditProfileActivity
     * @see EntrantScreenActivity
     * @see SettingsActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // get views
        bottomNavView = findViewById(R.id.bottom_nav);
        bottomNavView.setSelectedItemId(R.id.bottom_profile);

        entrantNameTV = findViewById(R.id.name_TV_profile_screen);
        entrantEmailTV = findViewById(R.id.email_TV_profile_screen);
        entrantPhoneTV = findViewById(R.id.phone_number_TV_profile_screen);
        entrantPhotoIV = findViewById(R.id.profile_photo_IV);
        editProfileButton = findViewById(R.id.edit_profile_button);

        // get database reference and the current user's android id
        db = FirebaseFirestore.getInstance();
        androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // set up bottom navigation between home, profile and settings
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

        // get user profile from database and sett textviews
        showProfile();
        /*
        String letter = entrantNameTV.toString().substring(0, 1).toUpperCase();
        TextDrawable drawable = new TextDrawable.Builder()
                .setColor(Color.parseColor("#C7C2EE"))
                .setShape(TextDrawable.SHAPE_ROUND_RECT)
                .setText(letter)
                .build();

        entrantPhotoIV.setImageDrawable(drawable);

         */

        // button to start edit profile activity
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
            }
        });

    }

    /**
     * Shows the user's current profile data on the screen
     */
    private void showProfile() {
        // get user from firebase
        db.collection("entrants")
                .whereEqualTo("id", androidID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            // set textviews
                            entrantNameTV.append(document.getString("name"));
                            entrantEmailTV.append(document.getString("email"));
                            String phone = document.getString("phone");
                            if (phone != null && !phone.isEmpty()) {
                                entrantPhoneTV.append(phone);
                            } else {
                                entrantPhoneTV.append("No phone number provided");
                            }
                            String letter = document.getString("name").substring(0, 1);
                            TextDrawable drawable = new TextDrawable.Builder()
                                    .setColor(Color.parseColor("#C7C2EE"))
                                    .setShape(TextDrawable.SHAPE_ROUND_RECT)
                                    .setRadius(10)
                                    .setText(letter)
                                    .setTextColor(Color.BLACK)
                                    .build();
                            entrantPhotoIV.setImageDrawable(drawable);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}