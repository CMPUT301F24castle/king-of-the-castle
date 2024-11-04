// EventListAdapter.java
package com.example.king_of_the_castle_project;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cursoradapter.widget.CursorAdapter;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class EventListAdapter extends CursorAdapter {
    private List<Event> events;
    private String entrantID;
    private FirebaseFirestore db;

    public EventListAdapter(Context context, List<Event> events, String entrantID) {
        super(context, null, 0);
        this.events = events;
        this.entrantID = entrantID;
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_waitlist_pending, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView title = view.findViewById(R.id.event_title);
        Button viewDetailsButton = view.findViewById(R.id.view_details_button);
        Button leaveEventButton = view.findViewById(R.id.leave_event_button);

        int position = cursor.getPosition();
        Event event = events.get(position);

        title.setText(event.getName());

        viewDetailsButton.setOnClickListener(v -> {
            // Add code to view details (Optional)
        });

        leaveEventButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Leave Event")
                    .setMessage("Are you sure you want to leave this event?")
                    .setPositiveButton("Yes", (dialog, which) -> leaveEvent(event.getName(), position))
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void leaveEvent(String eventName, int position) {
        db.collection("events").document(eventName)
                .update("waitingList", FieldValue.arrayRemove(entrantID))
                .addOnSuccessListener(aVoid -> {
                    events.remove(position);
                    notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle error
                });
    }
}
