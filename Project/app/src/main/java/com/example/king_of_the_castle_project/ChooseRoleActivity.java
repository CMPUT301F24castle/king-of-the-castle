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

/**
Screen to change between being an entrant/admin/organizer
 */
public class ChooseRoleActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    /**
     * Default method that performs basic application startup logic
     * @param savedInstanceState
     *          If there was an Instance saved, saved instances restores it
     */
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
                    // firebase
                    db = FirebaseFirestore.getInstance();
                    // make sure they are an administrator
                    String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    db.collection("administrators")
                                    .document(androidId)
                                    .get()
                                    .addOnCompleteListener(task -> {
                                        if (task.getResult().exists()) {
                                            // If they are an administrator start the admin screen
                                            Intent intent = new Intent(ChooseRoleActivity.this, AdministratorDashboardActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(ChooseRoleActivity.this, "Not a registered administrator", Toast.LENGTH_LONG).show();
                                        }
                                    });
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
