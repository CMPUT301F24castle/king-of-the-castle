package com.example.king_of_the_castle_project;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import androidx.appcompat.app.AppCompatActivity;

/*
Screen to change between being an entrant/admin/organizer
 */
public class ChooseRoleActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.role_change_screen);

        // reference the spinner view
        Spinner chooseRoleSpinner = findViewById(R.id.choose_role_spinner);

        // setup spinner items
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.roles_list_spinner,
                android.R.layout.simple_spinner_item
        );

        // set the dropdown view resource for the adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // assign adapter to spinner
        chooseRoleSpinner.setAdapter(adapter);

        // reference button view
        Button confirmRoleButton = findViewById(R.id.confirm_role_button);

        // set OnClickListener on button
        confirmRoleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle button click
                String selectedRole = chooseRoleSpinner.getSelectedItem().toString();

                // goto different screens
                if (selectedRole.equals("Entrant")){
                    Intent intent = new Intent(ChooseRoleActivity.this, EntrantScreenActivity.class);
                    startActivity(intent);

                } else if (selectedRole.equals("Organizer")){
                    Intent intent = new Intent(ChooseRoleActivity.this, OrganizerActivity.class);
                    startActivity(intent);

                } else if (selectedRole.equals("Administrator")){
                    // make sure they are an administrator - NOT IMPLEMENTED YET
//                    String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//                    Boolean adminFlag = false;
//                    db.collection(androidId)
//                                    .get()
//                                    .addOnCompleteListener(task -> {
//                                        if (task.isSuccessful()) {
//
//                                        }
//                                    })
                    Intent intent = new Intent(ChooseRoleActivity.this, AdministratorDashboardActivity.class);
                    startActivity(intent);

                } else {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast_notification_layout, findViewById(R.id.custom_toast_container));
                    TextView text = layout.findViewById(R.id.toast_text);
                    text.setText("ERROR. Please enter a valid role.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.setGravity(Gravity.BOTTOM, 0, 100);
                    toast.show();
                }
            }
        });
    }
}
