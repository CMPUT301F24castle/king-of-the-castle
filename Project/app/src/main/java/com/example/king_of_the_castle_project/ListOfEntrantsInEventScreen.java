package com.example.king_of_the_castle_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Activity that shows the organiser a list of entrants in a specific waitlist
 */
public class ListOfEntrantsInEventScreen extends AppCompatActivity {
    private ListView listOfEntrants;
    private ArrayList<String> filteredList;
    private ArrayList<String> waitlist;
    private Button returnBtn;
    private WaitListAdapter waitListAdapter;
    private TextView noResults;
    private SearchView searchBar;
    private Map<String, String> idToNameMap = new HashMap<>();
    private FirebaseFirestore db;

    /**
     * Default method that performs basic application startup logic
     * @param savedInstanceState
     *          If there was an Instance saved, saved instances restores it
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_entrants_in_event_screen);

        listOfEntrants = findViewById(R.id.list_of_entrants);
        returnBtn = findViewById(R.id.return_button_LOE_screen);
        noResults = findViewById(R.id.no_results_textview);
        searchBar = findViewById(R.id.search_bar);

        waitlist = (ArrayList<String>) getIntent().getSerializableExtra("Waitlist");
        filteredList = new ArrayList<>(waitlist);

        // Set the adapter after data is loaded
        waitListAdapter = new WaitListAdapter(this, filteredList);
        listOfEntrants.setAdapter(waitListAdapter);

        db = FirebaseFirestore.getInstance();
        idToNameMap = new HashMap<>();

        db.collection("entrants")
                .whereIn("id", waitlist)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getString("id");
                            String name = document.getString("name");
                            idToNameMap.put(id, name);
                        }
                    }
                });

        // shows no result textview if the waitlist is empty
        if (filteredList.isEmpty()) {
            noResults.setVisibility(View.VISIBLE);
            listOfEntrants.setVisibility(View.GONE);
        }

        // Set up the SearchView to filter the ListView
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterEntrants(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterEntrants(newText);
                return false;
            }
        });

            // goes back to previous activity
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    /**
     * Shows a list of entrants based on the text in the searchbar
     * @param query
     *          the text in the search bar
     */
    private void filterEntrants(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(waitlist);
        } else {
            for (String id: waitlist) {
                String name = idToNameMap.get(id).toLowerCase();
                if (name.contains(query.toLowerCase())) {
                    filteredList.add(id);
                }
            }
        }
        waitListAdapter.notifyDataSetChanged();
    }
}