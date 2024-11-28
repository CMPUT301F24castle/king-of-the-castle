package com.example.king_of_the_castle_project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    private EditText nameET;
    private EditText emailET;
    private EditText phoneET;

    private AppCompatButton confirmBtn;
    private AppCompatButton returnBut;
    private Button updatePhotoBtn;
    private Button deletePhotoBtn;
    private ImageView profilePhotoIV;

    private FirebaseFirestore db;
    private String androidID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        nameET = findViewById(R.id.entrant_name_edit_text_EP);
        emailET = findViewById(R.id.entrant_email_edit_text_EP);
        phoneET = findViewById(R.id.entrant_phone_edit_text_EP);
        profilePhotoIV = findViewById(R.id.profile_photo_IV);

        confirmBtn = findViewById(R.id.confirm_button_EP);
        updatePhotoBtn = findViewById(R.id.update_photo_button_EP);
        deletePhotoBtn = findViewById(R.id.delete_photo_button_EP);
        returnBut = findViewById(R.id.return_button_EP_screen);

        db = FirebaseFirestore.getInstance();
        androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


        // set all edit texts to current values
        setCurrentValues();
        showProfileImg();

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editFirebaseValues();
            }
        });

        returnBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });

    }

    private void setCurrentValues() {
        db.collection("entrants")
                .whereEqualTo("id", androidID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            nameET.setText(document.getString("name"));
                            emailET.setText(document.getString("email"));
                            String phone = document.getString("phone");
                            if (phone != null && !phone.isEmpty()) {
                                phoneET.setText(phone);
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void editFirebaseValues() {
        String name = nameET.getText().toString().trim();
        String email = emailET.getText().toString().trim();
        String phone = phoneET.getText().toString().trim();

        // Check for empty name
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter Name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show();
            return;
        }

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!email.matches(emailPattern)) {
            Toast.makeText(this, "Please enter a valid Email", Toast.LENGTH_SHORT).show();
            return;
        }

        Entrant entrant = new Entrant(name, email, phone.isEmpty() ? null : phone, androidID);

        Map<String, Object> entrantData = new HashMap<>();
        entrantData.put("name", entrant.getName());
        entrantData.put("email", entrant.getEmail());
        entrantData.put("phone", entrant.getPhoneNumber());
        entrantData.put("id", entrant.getId());

        db.collection("entrants").document(androidID)
                .set(entrantData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
    }

    private void showProfileImg() {
        db.collection("entrants")
                .whereEqualTo("id", androidID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String letter = document.getString("name").substring(0, 1);
                            TextDrawable drawable = new TextDrawable.Builder()
                                    .setColor(Color.parseColor("#C7C2EE"))
                                    .setShape(TextDrawable.SHAPE_ROUND_RECT)
                                    .setRadius(10)
                                    .setText(letter)
                                    .setTextColor(Color.BLACK)
                                    .build();
                            profilePhotoIV.setImageDrawable(drawable);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}