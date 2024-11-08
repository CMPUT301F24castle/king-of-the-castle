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

public class MyWaitlistsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecyclerView lotteryResultsRecycler, lotteryPendingRecycler, acceptedRecycler;
    private WaitingListAdapter resultsAdapter, pendingAdapter, acceptedAdapter;
    private List<Event> lotteryResultsEvents = new ArrayList<>();
    private List<Event> lotteryPendingEvents = new ArrayList<>();
    private List<Event> acceptedEvents = new ArrayList<>();
    private String entrantID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrant_event_waitlist);



        db = FirebaseFirestore.getInstance();
        entrantID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("MyWaitlistsActivity", "Entrant ID: " + entrantID);


        lotteryResultsRecycler = findViewById(R.id.lottery_results_recycler);
        lotteryPendingRecycler = findViewById(R.id.lottery_pending_recycler);
        acceptedRecycler = findViewById(R.id.lottery_accepted_recycler);

        lotteryResultsRecycler.setLayoutManager(new LinearLayoutManager(this));
        lotteryPendingRecycler.setLayoutManager(new LinearLayoutManager(this));
        acceptedRecycler.setLayoutManager(new LinearLayoutManager(this));

        resultsAdapter = new WaitingListAdapter(lotteryResultsEvents, entrantID);
        pendingAdapter = new WaitingListAdapter(lotteryPendingEvents, entrantID);
        acceptedAdapter = new WaitingListAdapter(acceptedEvents, entrantID);

        lotteryResultsRecycler.setAdapter(resultsAdapter);
        lotteryPendingRecycler.setAdapter(pendingAdapter);
        acceptedRecycler.setAdapter(acceptedAdapter);

        loadEntrantWaitingLists();

        findViewById(R.id.return_button).setOnClickListener(v -> {
            finish();
        });
    }

    private void loadEntrantWaitingLists() {
        db.collection("events")
                .whereArrayContains("waitList", entrantID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("MyWaitlistsActivity", " goes in here " + entrantID);

                        //lotteryResultsEvents.clear();
                        lotteryPendingEvents.clear();
                        //acceptedEvents.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Event event = document.toObject(Event.class);
                            lotteryPendingEvents.add(event);
                            pendingAdapter.notifyDataSetChanged();

                            // Logic to classify the event

                        }

//                        // Notify adapters to refresh data
//                        resultsAdapter.notifyDataSetChanged();
//                        pendingAdapter.notifyDataSetChanged();
//                        acceptedAdapter.notifyDataSetChanged();
                    } else {
                        // Handle any errors here
                    }
                });
    }


}
