package com.example.king_of_the_castle_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

/**
 * Activity that shows the organiser a list of entrants in a specific waitlist
 */
public class ListOfEntrantsInEventScreen extends AppCompatActivity {
    private ListView listOfEntrants;
    private Button returnBtn;
    private WaitListAdapter waitListAdapter;
    private TextView noResults;
    private EditText searchBar;

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

        ArrayList<Entrant> waitlist = (ArrayList<Entrant>) getIntent().getSerializableExtra("Waitlist");

        waitListAdapter = new WaitListAdapter(this, waitlist);
        listOfEntrants.setAdapter(waitListAdapter);

        // shows no result textview if the waitlist is empty
        if (waitlist.size() == 0) {
            noResults.setVisibility(View.VISIBLE);
            listOfEntrants.setVisibility(View.GONE);
        }

        // goes back to previous activity
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}