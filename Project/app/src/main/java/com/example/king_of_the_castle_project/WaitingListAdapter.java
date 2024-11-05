package com.example.king_of_the_castle_project;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class WaitingListAdapter extends RecyclerView.Adapter<WaitingListAdapter.WaitlistViewHolder> {
    private final List<Event> events;
    private final String entrantID;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public WaitingListAdapter(List<Event> events, String entrantID) {
        this.events = events;
        this.entrantID = entrantID;
    }

    @NonNull
    @Override
    public WaitlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_waitlist_pending, parent, false);
        return new WaitlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WaitlistViewHolder holder, int position) {
        Event event = events.get(position);
        holder.eventTitle.setText(event.getName());

        holder.viewDetailsButton.setOnClickListener(v -> {
            // Handle view details functionality if required
        });

        holder.leaveEventButton.setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Leave Event")
                    .setMessage("Are you sure you want to leave this event?")
                    .setPositiveButton("Yes", (dialog, which) -> leaveEvent(event.getName(), position))
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    private void leaveEvent(String eventName, int position) {
        db.collection("events").document(eventName)
                .update("waitingList", FieldValue.arrayRemove(entrantID))
                .addOnSuccessListener(aVoid -> {
                    events.remove(position);
                    notifyItemRemoved(position);
                })
                .addOnFailureListener(e -> {
                    // Handle error
                });
    }

    static class WaitlistViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle;
        Button viewDetailsButton, leaveEventButton;

        WaitlistViewHolder(View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.event_title);
            viewDetailsButton = itemView.findViewById(R.id.view_details_button);
            leaveEventButton = itemView.findViewById(R.id.leave_event_button);
        }
    }
}
