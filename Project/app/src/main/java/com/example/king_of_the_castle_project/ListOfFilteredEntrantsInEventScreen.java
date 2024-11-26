package com.example.king_of_the_castle_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Activity that shows the organiser a list of entrants filtered by invited/cancelled/enrolled status
 */
public class ListOfFilteredEntrantsInEventScreen extends AppCompatActivity{
    private ListView listOfEntrants;
    private ArrayList<String> entrant_id_list;
    private ArrayList<Entrant> filteredList;
    private ArrayList<Entrant> entrant_list;
    private Button returnBtn;
    private TextView noResults;
    private SearchView searchBar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    EntrantListArrayAdapter entrantListAdapter;
    /**
     * Default method that performs basic application startup logic
     * @param savedInstanceState
     *          If there was an Instance saved, saved instances restores it
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_entrant_list_screen);

        // get views
        listOfEntrants = findViewById(R.id.list_of_entrants);
        returnBtn = findViewById(R.id.return_button_LOE_screen);
        noResults = findViewById(R.id.no_results_textview);
        searchBar = findViewById(R.id.search_bar);

        // get and store intent
        entrant_id_list = (ArrayList<String>) getIntent().getSerializableExtra("entrant_id_list");

        if (entrant_id_list.isEmpty()){
            noResults.setVisibility(View.VISIBLE);
            listOfEntrants.setVisibility(View.GONE);
        } else {
            // get all entrant objects from firebase and display them
            queryEntrants(entrant_id_list);
        }

        // Set up the SearchView to filter the ListView
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!entrant_list.isEmpty()){
                    filterEntrants(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!entrant_list.isEmpty()){
                    filterEntrants(newText);
                }
                return false;
            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Queries the Firestore database for entrants whose IDs match any of the given entrant IDs.
     *
     * @param entrantIds A list of strings representing the IDs of the entrants to be queried in the Firestore database.
     *
     * @see Entrant
     * @see EntrantListArrayAdapter
     */
    private void queryEntrants(ArrayList<String> entrantIds) {
        // query entrants collection in db where IDs are the same
                db.collection("entrants").whereIn("id", entrantIds)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                // Create Entrant object from document data
                                Entrant entrant = document.toObject(Entrant.class);
                                if (entrant_list != null) {
                                    entrant_list.add(entrant);
                                } else {
                                    // Handle null case or initialize the list
                                    entrant_list = new ArrayList<>();
                                    entrant_list.add(entrant);
                                }
                            }
                            filteredList = new ArrayList<>(entrant_list);
                            entrantListAdapter = new EntrantListArrayAdapter(this, filteredList);
                            listOfEntrants.setAdapter(entrantListAdapter);
                        }
                    } else {
                        // handle failure
                        Toast.makeText(getApplicationContext(), "Query failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Shows a list of entrants based on the text in the searchbar
     * @param query
     *          the text in the search bar
     *
     * @see Entrant
     */
    private void filterEntrants(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(entrant_list);
        } else {
            for (Entrant entrant: entrant_list) {
                String name = entrant.getName();
                if (name.contains(query.toLowerCase())) {
                    filteredList.add(entrant);
                }
            }
        }
        entrantListAdapter.notifyDataSetChanged();

        if (filteredList.isEmpty()){
            noResults.setVisibility(View.VISIBLE);
            listOfEntrants.setVisibility(View.GONE);
        } else {
            noResults.setVisibility(View.GONE);
            listOfEntrants.setVisibility(View.VISIBLE);
        }
    }
}
