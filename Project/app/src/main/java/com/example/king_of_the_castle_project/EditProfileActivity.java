package com.example.king_of_the_castle_project;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity that allows an entrant to edit their profile information (photo, name, email, phone number)
 */
public class EditProfileActivity extends AppCompatActivity {
    private EditText nameET;
    private EditText emailET;
    private EditText phoneET;

    private AppCompatButton confirmBtn;
    private AppCompatButton returnBut;
    private Button updatePhotoBtn;
    private Button deletePhotoBtn;

    private FirebaseFirestore db;
    private String androidID;

    /**
     * Default method that performs basic application startup logic
     * @param savedInstanceState
     *          If there was an Instance saved, saved instances restores it
     *
     * @see ProfileActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // get views
        nameET = findViewById(R.id.entrant_name_edit_text_EP);
        emailET = findViewById(R.id.entrant_email_edit_text_EP);
        phoneET = findViewById(R.id.entrant_phone_edit_text_EP);

        confirmBtn = findViewById(R.id.confirm_button_EP);
        updatePhotoBtn = findViewById(R.id.update_photo_button_EP);
        deletePhotoBtn = findViewById(R.id.delete_photo_button_EP);
        returnBut = findViewById(R.id.return_button_EP_screen);

        // get database reference and the current user's android id
        db = FirebaseFirestore.getInstance();
        androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


        // set all edit texts to current profile values
        setCurrentValues();

        // confirm any values changed in the EditTexts
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

    /**
     * Adds the current values of the user's profile to the EditText views
     */
    private void setCurrentValues() {
        // get user from database
        db.collection("entrants")
                .whereEqualTo("id", androidID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // set edit text values
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

    /**
     * Adds the current values in the EditText Views to the current user's collection in firebase
     *
     * @see Entrant
     * @see ProfileActivity
     */
    private void editFirebaseValues() {
        // get current values
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

        // create a new entrant with these values
        Entrant entrant = new Entrant(name, email, phone.isEmpty() ? null : phone, androidID);

        // create a hashmap for the entrant
        Map<String, Object> entrantData = new HashMap<>();
        entrantData.put("name", entrant.getName());
        entrantData.put("email", entrant.getEmail());
        entrantData.put("phone", entrant.getPhoneNumber());
        entrantData.put("id", entrant.getId());

        // add entrant's data to the database
        db.collection("entrants").document(androidID)
                .set(entrantData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // go back to Profile Activity
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
    }
}