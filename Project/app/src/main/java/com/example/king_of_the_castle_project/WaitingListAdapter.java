package com.example.king_of_the_castle_project;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entrant_event_waitlist_lot_pending_content, parent, false);
        return new WaitlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WaitlistViewHolder holder, int position) {
        Event event = events.get(position);
        holder.eventTitle.setText(event.getName());

        holder.viewDetailsButton.setOnClickListener(v -> {
            // not implemented yet
        });

        holder.leaveEventButton.setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Leave Event")
                    .setMessage("Are you sure you want to leave this event?")
                    .setPositiveButton("Yes", (dialog, which) -> leaveEvent(event.getName(), position, holder.itemView.getContext()))
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    private void leaveEvent(String eventName, int position, Context context) {
        db.collection("events").document(eventName)
                .update("waitingList", FieldValue.arrayRemove(entrantID))
                .addOnSuccessListener(aVoid -> {
                    events.remove(position);
                    notifyItemRemoved(position);

                    // Inflate custom toast layout
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View layout = inflater.inflate(R.layout.toast_notification_layout, null);

                    TextView toastText = layout.findViewById(R.id.toast_text);
                    toastText.setText("You've left the waiting list for this event. ");

                    // Create and show custom toast
                    Toast customToast = new Toast(context);
                    customToast.setDuration(Toast.LENGTH_SHORT);
                    customToast.setView(layout);
                    customToast.show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(
                            context,
                            "Failed to leave the event. Please try again.",
                            Toast.LENGTH_SHORT

                    ).show();
                });
    }

    static class WaitlistViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle;
        Button viewDetailsButton, leaveEventButton;

        WaitlistViewHolder(View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.entrant_event_list_item_name);
            viewDetailsButton = itemView.findViewById(R.id.entrant_event_list_details_button);
            leaveEventButton = itemView.findViewById(R.id.entrant_event_list_leave_wait_button);
        }
    }
}
