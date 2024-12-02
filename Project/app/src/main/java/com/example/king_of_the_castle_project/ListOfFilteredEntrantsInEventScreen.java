package com.example.king_of_the_castle_project;

import android.os.Bundle;
import android.util.Log;
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
    private ArrayList<Map<String, Object>> entrant_location_list;
    private ArrayList<Map<String, Object>> filteredLocationList;
    private Boolean geolocation_toggle;
    private String list_type;
    private String event_id;
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

        // get and store intents
        entrant_id_list = (ArrayList<String>) getIntent().getSerializableExtra("entrant_id_list");
        event_id = (String) getIntent().getSerializableExtra("event_id");
        geolocation_toggle = (Boolean) getIntent().getSerializableExtra("event_geolocation_toggle");
        list_type = (String) getIntent().getSerializableExtra("entrant_list_type");

        // initialize map list
        entrant_location_list = new ArrayList<>();
        entrant_list = new ArrayList<>();

        if (entrant_id_list.isEmpty()){
            noResults.setVisibility(View.VISIBLE);
            listOfEntrants.setVisibility(View.GONE);
        } else {
            // use different array adapters depending on geolocation
            if (geolocation_toggle && list_type.equals("waitList")){
                queryEntrantsWithLocation(entrant_id_list, event_id, entrant_location_list);
            } else {
                // get all entrant objects from firebase and display them
                Log.d("marko", entrant_id_list.get(0));
                queryEntrants(entrant_id_list);
            }
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
                                String pfpData = document.getString("profileImg");
                                entrant.setPfpData(pfpData);
                                Log.d("marko", "entrant id: " + entrant.getId());
                                Log.d("marko", "entrant pfp: " + entrant.getPfpData());
                                entrant_list.add(entrant);
                            }
                            Log.d("marko", "testing");
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
        if (geolocation_toggle && list_type.equals("waitList")) {
            filteredLocationList.clear();

            // get the list of entrants that should be shown based on the query
            if (query.isEmpty()) {
                filteredLocationList.addAll(entrant_location_list);
            } else {
                for (Map<String, Object> entrant: entrant_location_list) {
                    String name = (String) entrant.get("entrant");
                    if (name != null && name.toLowerCase().contains(query.toLowerCase())) {
                        filteredLocationList.add(entrant);
                    }
                }
            }

            // update what is shown on the screen
            entrantListAdapter.notifyDataSetChanged();

            if (filteredLocationList.isEmpty()){
                noResults.setVisibility(View.VISIBLE);
                listOfEntrants.setVisibility(View.GONE);
            } else {
                noResults.setVisibility(View.GONE);
                listOfEntrants.setVisibility(View.VISIBLE);
            }
        }
        else {
            filteredList.clear();

            // get the list of entrants that should be shown based on the query
            if (query.isEmpty()) {
                filteredList.addAll(entrant_list);
            } else {
                for (Entrant entrant: entrant_list) {
                    String name = entrant.getName();
                    if (name.toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(entrant);
                    }
                }
            }

            // update what is shown on the screen
            entrantListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Queries the Firestore database for entrants whose IDs match any of the given entrant IDs and their location
     *
     * @param entrantIds A list of strings representing the IDs of the entrants to be queried in the Firestore database.
     * @param event_id The ID of the event for which the entrants' location data is being queried.
     * @param entrant_location_list A list to store maps containing entrant data and their location information (latitude and longitude).
     *
     * @see Entrant
     * @see EntrantListArrayAdapterWithLocation
     */
    private void queryEntrantsWithLocation(ArrayList<String> entrantIds, String event_id, ArrayList<Map<String, Object>> entrant_location_list) {
        // get entrant object from firebase with entrant id
        db.collection("entrants").whereIn("id", entrantIds)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                // make entrant object
                                Entrant entrant = document.toObject(Entrant.class);
                                String pfpData = document.getString("profileImg");
                                entrant.setPfpData(pfpData);
                                // get event based on id
                                db.collection("events").document(event_id)
                                        .get()
                                        .addOnSuccessListener(eventSnapshot -> {
                                            if (eventSnapshot.exists()) {
                                                // get waitlist in event
                                                List<Map<String, Object>> waitList =
                                                        (List<Map<String, Object>>) eventSnapshot.get("waitList");
                                                if (waitList != null) {
                                                    // iterate through waitlist in event
                                                    for (Map<String, Object> entry : waitList) {
                                                        // if entrant id found get their location
                                                        if (entrant.getId().equals(entry.get("entrantID"))) {
                                                            // cast number to double and store it
                                                            Object value1 = entry.get("latitude");
                                                            double doubleValue1 = 0;
                                                            if (value1 instanceof Number) {
                                                                doubleValue1 = ((Number) value1).doubleValue();
                                                            }
                                                            double latitude = doubleValue1;
                                                            Object value2 = entry.get("longitude");
                                                            double doubleValue2 = 0;
                                                            if (value2 instanceof Number) {
                                                                doubleValue2 = ((Number) value2).doubleValue();
                                                            }
                                                            double longitude = doubleValue2;
                                                            // create map to store info
                                                            Map<String, Object> entrantData = new HashMap<>();
                                                            entrantData.put("entrant", entrant);
                                                            entrantData.put("latitude", latitude);
                                                            entrantData.put("longitude", longitude);
                                                            // add map to array
                                                            entrant_location_list.add(entrantData);
                                                            // since we found entrant stop iterating through the waitlist
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            // once the loop ends and the list is populated, set the adapter
                                            if (entrant_location_list.size() > 0) {
                                                // create the adapter with the list and set it to the ListView
                                                filteredLocationList = new ArrayList<>(entrant_location_list);
                                                EntrantListArrayAdapterWithLocation adapter = new EntrantListArrayAdapterWithLocation(ListOfFilteredEntrantsInEventScreen.this, filteredLocationList);
                                                listOfEntrants.setAdapter(adapter);
                                            }
                                        });
                            }
                        }
                    } else {
                        // handle failure in the query
                        Toast.makeText(getApplicationContext(), "Failed to retrieve entrants.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
