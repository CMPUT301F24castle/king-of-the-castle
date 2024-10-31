package com.example.king_of_the_castle_project;

import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginScreenActivity extends AppCompatActivity {
    private EditText entrantName, entrantEmail, entrantPhone;
    private Button SignUpButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        db = FirebaseFirestore.getInstance();

        // android device id
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        entrantName = findViewById(R.id.entrant_name_edit_text);
        entrantEmail = findViewById(R.id.entrant_email_edit_text);
        entrantPhone = findViewById(R.id.entrant_phone_edit_text);
        SignUpButton = findViewById(R.id.sign_up_button);

        SignUpButton.setOnClickListener(v -> saveEntrantData(androidId));
    }

    private void saveEntrantData(String androidId) {

        String name = entrantName.getText().toString().trim();
        String email = entrantEmail.getText().toString().trim();
        String phone = entrantPhone.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter Name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.isEmpty()){
            Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show();
        }

        // Create a map for the entrant's data
        Map<String, Object> entrantData = new HashMap<>();
        entrantData.put("name", name);
        entrantData.put("email", email);
        if (!phone.isEmpty()) entrantData.put("phone", phone);

        // Save to Firestore with androidId as the document ID
        db.collection("entrants").document(androidId)
                .set(entrantData)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}