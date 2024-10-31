package com.example.king_of_the_castle_project;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyWaitlistsActivity extends AppCompatActivity {
    private RecyclerView waitingListRecyclerView;
    private WaitingListAdapter adapter;
    private FirebaseFirestore db;
    private String entrantID; // Set this to the current entrant’s ID
    private List<Event> eventList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrant_event_waitlist);

        waitingListRecyclerView = findViewById(R.id.lottery_pending_recycler);
        waitingListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        loadEntrantWaitingList();
    }

    private void loadEntrantWaitingList() {
        db.collection("events")
                .whereArrayContains("waitingList", entrantID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        eventList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Event event = document.toObject(Event.class);
                            eventList.add(event);
                        }
                        adapter = new WaitingListAdapter(this, eventList, entrantID);
                        waitingListRecyclerView.setAdapter(adapter);
                    }
                });
    }
}
