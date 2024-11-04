// MyWaitingListsActivity.java
package com.example.king_of_the_castle_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyWaitlistsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private ListView lotteryResultsList, lotteryPendingList, acceptedList;
    private EventListAdapter resultsAdapter, pendingAdapter, acceptedAdapter;
    private List<Event> lotteryResultsEvents = new ArrayList<>();
    private List<Event> lotteryPendingEvents = new ArrayList<>();
    private List<Event> acceptedEvents = new ArrayList<>();
    private String entrantID; // Entrant's unique ID

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrant_event_waitlist);

        db = FirebaseFirestore.getInstance();
        lotteryResultsList = findViewById(R.id.lottery_results_recycler);
        lotteryPendingList = findViewById(R.id.lottery_pending_recycler);
        acceptedList = findViewById(R.id.lottery_accepted_recycler);

        resultsAdapter = new EventListAdapter(this, lotteryResultsEvents, entrantID);
        pendingAdapter = new EventListAdapter(this, lotteryPendingEvents, entrantID);
        acceptedAdapter = new EventListAdapter(this, acceptedEvents, entrantID);

        lotteryResultsList.setAdapter(resultsAdapter);
        lotteryPendingList.setAdapter(pendingAdapter);
        acceptedList.setAdapter(acceptedAdapter);

        loadEntrantWaitingLists();

        findViewById(R.id.return_button).setOnClickListener(v -> {
            Intent intent = new Intent(MyWaitlistsActivity.this, EntrantScreenActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadEntrantWaitingLists() {
        db.collection("events")
                .whereArrayContains("waitingList", entrantID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        lotteryResultsEvents.clear();
                        lotteryPendingEvents.clear();
                        acceptedEvents.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Event event = document.toObject(Event.class);

                            // logic to classify
                        }


                    } else {
                        // Handle any errors here
                    }
                });
    }
}
