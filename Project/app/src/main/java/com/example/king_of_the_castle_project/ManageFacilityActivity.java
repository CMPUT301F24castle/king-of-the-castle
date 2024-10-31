package com.example.king_of_the_castle_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ManageFacilityActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                // check if facility exists in database
                // if not
                Facility facility = new Facility(facilityName, facilityLocation, facilityNumber);
                // Send facility to firebase
                /* else
                getFacility somehow, then facility.setName(facilityName, location, etc);
                Update facility in firebase
                 */
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
}
