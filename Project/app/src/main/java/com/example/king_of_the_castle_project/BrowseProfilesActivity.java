package com.example.king_of_the_castle_project;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Activity that shows the admin a list of all entrants and allows the admin to remove the entrant of their profile
 */
public class BrowseProfilesActivity extends AppCompatActivity {
    private ListView list_of_entrants;
    private TextView noResultsTextView;
    private AppCompatButton returnButton;
    private ArrayList<Entrant> entrant_list;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Default method that performs basic application startup logic
     * @param savedInstanceState
     *          If there was an Instance saved, saved instances restores it
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_profile_browsing_screen);

        // get views
        list_of_entrants = findViewById(R.id.list_of_entrants);
        noResultsTextView = findViewById(R.id.no_results_textview);
        returnButton = findViewById(R.id.return_button_LOE_screen);

        // init empty entrant list
        entrant_list = new ArrayList<>();

        // create adapter
        ProfileAdminArrayAdapter arrayAdapter = new ProfileAdminArrayAdapter(this, entrant_list);

        // set view to adapter
        list_of_entrants.setAdapter(arrayAdapter);

        // query entire entrants collection in db and add to list
        db.collection("entrants")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Entrant entrant = document.toObject(Entrant.class);
                            entrant_list.add(entrant);
                            String pfpData = document.getString("profileImg");
                            entrant.setPfpData(pfpData);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                    else {
                        // handle failure
                        Toast.makeText(getApplicationContext(), "Query failed", Toast.LENGTH_SHORT).show();
                    }
                });

        // set onclick for button returning to prev screen
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
