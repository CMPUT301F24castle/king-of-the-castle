package com.example.king_of_the_castle_project;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity that allows an entrant to log in by providing basic personal information.
 * The information is saved to Firebase Firestore, identified by the device's unique ID.
 */
public class LoginScreenActivity extends AppCompatActivity {
    EditText entrantName;
    EditText entrantEmail;
    EditText entrantPhone;
    private Button signUpButton;
    FirebaseFirestore db;

    /**
     * Initializes the activity and sets up the form fields and sign-up button.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down, this contains the data it most
     *                           recently supplied in onSaveInstanceState. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        db = FirebaseFirestore.getInstance();

        // Retrieve the unique Android device ID.
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        entrantName = findViewById(R.id.entrant_name_edit_text);
        entrantEmail = findViewById(R.id.entrant_email_edit_text);
        entrantPhone = findViewById(R.id.entrant_phone_edit_text);
        signUpButton = findViewById(R.id.sign_up_button);

        // Set a click listener on the sign-up button to save entrant data and navigate to the next screen.
        signUpButton.setOnClickListener(v -> saveEntrantData(androidId, isSuccess -> {
            if (isSuccess) {
                Intent intent = new Intent(this, ChooseRoleActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    /**
     * Saves the entrant's data to Firebase Firestore. This includes their name, email, phone (optional),
     * and unique device ID.
     *
     * @param androidId The unique Android device ID used as the document ID in Firestore.
     * @param listener  A callback to notify whether the save operation was successful.
     */
    void saveEntrantData(String androidId, OnSaveCompleteListener listener) {
        String name = entrantName.getText().toString().trim();
        String email = entrantEmail.getText().toString().trim();
        String phone = entrantPhone.getText().toString().trim();

        // Check for empty name
        if (name.isEmpty()) {

            LayoutInflater inflater = LayoutInflater.from(this);
            View layout = inflater.inflate(R.layout.toast_notification_layout, null);

            TextView toastText = layout.findViewById(R.id.toast_text);
            toastText.setText("Please enter name");

            // Create and show custom toast
            Toast customToast = new Toast(this);
            customToast.setDuration(Toast.LENGTH_SHORT);
            customToast.setView(layout);
            customToast.show();
            return;
        }

        if (email.isEmpty()) {

            LayoutInflater inflater = LayoutInflater.from(this);
            View layout = inflater.inflate(R.layout.toast_notification_layout, null);

            TextView toastText = layout.findViewById(R.id.toast_text);
            toastText.setText("Please enter Email");

            // Create and show custom toast
            Toast customToast = new Toast(this);
            customToast.setDuration(Toast.LENGTH_SHORT);
            customToast.setView(layout);
            customToast.show();
            return;
        }

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!email.matches(emailPattern)) {

            LayoutInflater inflater = LayoutInflater.from(this);
            View layout = inflater.inflate(R.layout.toast_notification_layout, null);

            TextView toastText = layout.findViewById(R.id.toast_text);
            toastText.setText("Please enter valid email");

            // Create and show custom toast
            Toast customToast = new Toast(this);
            customToast.setDuration(Toast.LENGTH_SHORT);
            customToast.setView(layout);
            customToast.show();
            return;
        }

        if (!phone.isEmpty()) {
            // Check if the phone number is numeric
            if (!phone.matches("\\d+")) {
                LayoutInflater inflater = LayoutInflater.from(this);
                View layout = inflater.inflate(R.layout.toast_notification_layout, null);

                TextView toastText = layout.findViewById(R.id.toast_text);
                toastText.setText("Phone number must be numeric");

                // Create and show custom toast
                Toast customToast = new Toast(this);
                customToast.setDuration(Toast.LENGTH_SHORT);
                customToast.setView(layout);
                customToast.show();
                return;
            }

            // Check if the phone number has exactly 10 digits
            if (phone.length() != 10) {

                LayoutInflater inflater = LayoutInflater.from(this);
                View layout = inflater.inflate(R.layout.toast_notification_layout, null);

                TextView toastText = layout.findViewById(R.id.toast_text);
                toastText.setText("Phone number must be 10 digits long");

                // Create and show custom toast
                Toast customToast = new Toast(this);
                customToast.setDuration(Toast.LENGTH_SHORT);
                customToast.setView(layout);
                customToast.show();
                return;
            }
        }

        Entrant entrant = new Entrant(name, email, phone.isEmpty() ? null : phone, androidId);

        Map<String, Object> entrantData = new HashMap<>();
        entrantData.put("name", entrant.getName());
        entrantData.put("email", entrant.getEmail());
        entrantData.put("phone", entrant.getPhoneNumber());
        entrantData.put("id", entrant.getId());
        entrantData.put("profileImg", null);

        db.collection("entrants").document(androidId)
                .set(entrantData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                    listener.onComplete(true);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    listener.onComplete(false);
                });
    }

    /**
     * A callback interface to notify when data saving is completed.
     */
    interface OnSaveCompleteListener {
        /**
         * Called when data save operation is completed.
         *
         * @param isSuccess True if the data was saved successfully, false otherwise.
         */
        void onComplete(boolean isSuccess);
    }
}
