package com.example.king_of_the_castle_project;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity that displays the entrant's waiting lists for events.
 * Events are displayed in three categories: lottery results, pending, and accepted.
 */
public class MyWaitlistsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecyclerView lotteryResultsRecycler, lotteryPendingRecycler, acceptedRecycler;
    private WaitingListAdapter resultsAdapter, pendingAdapter, acceptedAdapter;
    private List<Event> lotteryResultsEvents = new ArrayList<>();
    private List<Event> lotteryPendingEvents = new ArrayList<>();
    private List<Event> acceptedEvents = new ArrayList<>();
    private String entrantID;

    /**
     * Initializes the activity, sets up the RecyclerViews, adapters, and loads entrant waiting lists.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this contains the data it most recently supplied in onSaveInstanceState.
     *                           Otherwise, it is null.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrant_event_waitlist);

        db = FirebaseFirestore.getInstance();
        entrantID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("MyWaitlistsActivity", "Entrant ID: " + entrantID);

        // Initialize RecyclerViews for each category
        lotteryResultsRecycler = findViewById(R.id.lottery_results_recycler);
        lotteryPendingRecycler = findViewById(R.id.lottery_pending_recycler);
        acceptedRecycler = findViewById(R.id.lottery_accepted_recycler);

        lotteryResultsRecycler.setLayoutManager(new LinearLayoutManager(this));
        lotteryPendingRecycler.setLayoutManager(new LinearLayoutManager(this));
        acceptedRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Set up adapters for each category
        resultsAdapter = new WaitingListAdapter(lotteryResultsEvents, entrantID);
        pendingAdapter = new WaitingListAdapter(lotteryPendingEvents, entrantID);
        acceptedAdapter = new WaitingListAdapter(acceptedEvents, entrantID);

        lotteryResultsRecycler.setAdapter(resultsAdapter);
        lotteryPendingRecycler.setAdapter(pendingAdapter);
        acceptedRecycler.setAdapter(acceptedAdapter);

        loadEntrantWaitingLists();

        // Set up the return button to finish the activity and return to the previous screen
        findViewById(R.id.return_button).setOnClickListener(v -> {
            finish();
        });
    }

    /**
     * Loads the entrant's waiting list data from Firestore and classifies events into categories.
     * Currently, adds all events containing the entrant's ID in the "waitList" array to the pending events list.
     * Future logic could be added to classify events into different categories.
     */
    private void loadEntrantWaitingLists() {
        db.collection("events")
                .whereArrayContains("waitList", entrantID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("MyWaitlistsActivity", "Loaded waitlists for entrant ID: " + entrantID);

                        // Clear pending events to refresh data
                        this.lotteryPendingEvents.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Event event = document.toObject(Event.class);
                            this.lotteryPendingEvents.add(event);
                            this.pendingAdapter.notifyDataSetChanged();

                            // Logic to classify the event could be added here
                        }

                    } else {
                        // Handle any errors encountered during Firestore query
                    }
                });
    }
}
