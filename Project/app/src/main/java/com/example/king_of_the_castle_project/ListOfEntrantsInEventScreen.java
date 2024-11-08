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

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Default method that performs basic application startup logic
     * @param savedInstanceState
     *          If there was an Instance saved, saved instances restores it
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_entrants_in_event_screen);

        /* Since it's coming from the my events screen I'll assume the waitlist will
           be passed as an intent to this screen.*/

        /* Intent example I used to test in Main activity (assuming waitlist is an arraylist):
        Entrant e1 = new Entrant("Ty Collins", "Ty@gmail.com", "7803832211", "78gd3f89h4d");
        Entrant e2 = new Entrant("Albert Einstien", "Al@gmail.com", "7803932290", "8934hf03f");
        ArrayList<Entrant> EntrantList = new ArrayList<>();
        //EntrantList.add(e1);
        //EntrantList.add(e2);

        Intent i = new Intent(MainActivity.this, ListOfEntrantsInEventScreen.class);
        i.putExtra("Waitlist", EntrantList);
        startActivity(i);

        * */

        listOfEntrants = findViewById(R.id.list_of_entrants);
        returnBtn = findViewById(R.id.return_button_LOE_screen);
        noResults = findViewById(R.id.no_results_textview);
        searchBar = findViewById(R.id.search_bar);

        waitlist = (ArrayList<String>) getIntent().getSerializableExtra("Waitlist");
        filteredList = new ArrayList<>(waitlist);

        waitListAdapter = new WaitListAdapter(this, filteredList);
        listOfEntrants.setAdapter(waitListAdapter);

        // shows no result textview if the waitlist is empty
        if (filteredList.size() == 0) {
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
            for (String entrant : waitlist) {
                if (entrant.toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(entrant);
                }
            }
        }
        waitListAdapter.notifyDataSetChanged();
    }
}

/*
public class ListOfEntrantsInEventScreen extends AppCompatActivity {
    private ListView listOfEntrants;
    private ArrayList<Entrant> filteredList;
    private ArrayList<Entrant> waitlist;
    private Button returnBtn;
    private WaitListAdapter waitListAdapter;
    private TextView noResults;
    private SearchView searchBar;

    /**
     * Default method that performs basic application startup logic
     * @param savedInstanceState
     *          If there was an Instance saved, saved instances restores it
     */
/*
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list_of_entrants_in_event_screen);*/

        /* Since it's coming from the my events screen I'll assume the waitlist will
           be passed as an intent to this screen.*/

        /* Intent example I used to test in Main activity (assuming waitlist is an arraylist):
        Entrant e1 = new Entrant("Ty Collins", "Ty@gmail.com", "7803832211", "78gd3f89h4d");
        Entrant e2 = new Entrant("Albert Einstien", "Al@gmail.com", "7803932290", "8934hf03f");
        ArrayList<Entrant> EntrantList = new ArrayList<>();
        //EntrantList.add(e1);
        //EntrantList.add(e2);

        Intent i = new Intent(MainActivity.this, ListOfEntrantsInEventScreen.class);
        i.putExtra("Waitlist", EntrantList);
        startActivity(i);

        * */
/*
    listOfEntrants = findViewById(R.id.list_of_entrants);
    returnBtn = findViewById(R.id.return_button_LOE_screen);
    noResults = findViewById(R.id.no_results_textview);
    searchBar = findViewById(R.id.search_bar);

    waitlist = (ArrayList<Entrant>) getIntent().getSerializableExtra("Waitlist");
    filteredList = new ArrayList<>(waitlist);

    waitListAdapter = new WaitListAdapter(this, filteredList);
    listOfEntrants.setAdapter(waitListAdapter);

    // shows no result textview if the waitlist is empty
    if (filteredList.size() == 0) {
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
/*
private void filterEntrants(String query) {
    filteredList.clear();
    if (query.isEmpty()) {
        filteredList.addAll(waitlist);
    } else {
        for (Entrant entrant : waitlist) {
            if (entrant.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(entrant);
            }
        }
    }
    waitListAdapter.notifyDataSetChanged();
}
}
 */