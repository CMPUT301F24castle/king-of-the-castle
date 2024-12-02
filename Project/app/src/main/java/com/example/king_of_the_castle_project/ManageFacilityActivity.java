package com.example.king_of_the_castle_project;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Class used to handle facility creation and editing
 */
public class ManageFacilityActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private String androidId;
    /**
     * Default method for basic startup logic
     * @param savedInstanceState
     *      If there was an Instance saved, saved instances restores it
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // firebase stuff
        db = FirebaseFirestore.getInstance();
        // event stuff
        setContentView(R.layout.manage_facility_screen);
        EditText facilityNameEditText = findViewById(R.id.facility_name_edit);
        EditText facilityLocationEditText = findViewById(R.id.facility_location_edit);
        EditText facilityNumberEditText = findViewById(R.id.facility_number_edit);

        Button editFacilityButton = findViewById(R.id.edit_facility_button);
        Button editFacilityReturnButton = findViewById(R.id.return_button);

        editFacilityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send the information to the firebase
                String facilityName = facilityNameEditText.getText().toString();
                String facilityLocation = facilityLocationEditText.getText().toString();
                String facilityNumber = facilityNumberEditText.getText().toString();

                Facility facility = new Facility(facilityName, facilityLocation, facilityNumber);
                // Send facility to or edit if it exists
                androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                sendToFirebase(facility, androidId);
                Intent resultIntent = new Intent();
                // Put the data to pass back
                resultIntent.putExtra("facilityModified", facilityName);
                // Set result
                setResult(RESULT_OK, resultIntent);
                // Return
                finish();
            }
        });
        editFacilityReturnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        // From main lol
        /*
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
    }

    /**
     * Sends a facility to firebase
     * @param facility
     *      A facility object, containing the information
     * @param androidId
     *      The ID of the organizer, to be used as a document name (1 to 1)
     */
    private void sendToFirebase(Facility facility, String androidId) {
        // Create a new object that can be stored for event that does not use waitlist because it will bug :)
        String name = facility.getName();
        // Create new document or add to collection
        db.collection("facilities")
                .document(androidId)
                .set(facility)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firestore", "Successful send to firebase");
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore: ", "Failed to add event to firebase", e);
                    // displayToastNotification("Failed to add event" + e.getMessage());
                });
    }
}
