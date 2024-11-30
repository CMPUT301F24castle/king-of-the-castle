package com.example.king_of_the_castle_project;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
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

/**
 * Adapter for displaying a list of events on the waiting list in a RecyclerView.
 * Allows entrants to view event details (if implemented) or leave the waitlist for an event.
 */
public class WaitingListAdapter extends RecyclerView.Adapter<WaitingListAdapter.WaitlistViewHolder> {
    private final List<Event> events;
    private final String entrantID;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Constructs a new WaitingListAdapter.
     *
     * @param events   The list of events the entrant is on the waitlist for.
     * @param entrantID The unique ID of the entrant, used for identifying their waitlist status.
     */
    public WaitingListAdapter(List<Event> events, String entrantID) {
        this.events = events;
        this.entrantID = entrantID;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder to represent an item.
     *
     * @param parent   The parent ViewGroup.
     * @param viewType The view type of the new View.
     * @return A new WaitlistViewHolder that holds a View for each waitlist item.
     */
    @NonNull
    @Override
    public WaitlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("AdapterDebug", "onCreateViewHolder called.");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entrant_event_waitlist_lot_pending_content, parent, false);
        return new WaitlistViewHolder(view);
    }

    /**
     * Binds data to the view for each event on the waitlist, setting up button actions.
     *
     * @param holder   The ViewHolder to bind data to.
     * @param position The position of the event in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull WaitlistViewHolder holder, int position) {
        Event event = events.get(position);
        Log.d("AdapterDebug", "Binding view for event: " + event.getName());
        holder.eventTitle.setText(event.getName());
        holder.eventStart.setText(event.getTime());
        holder.eventDate.setText(event.getDate());

        // Set up View Details button (functionality not yet implemented)
        holder.viewDetailsButton.setOnClickListener(v -> {
            // Placeholder for view details functionality
        });

        // Set up Leave Event button with a confirmation dialog
        holder.leaveEventButton.setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Leave Event")
                    .setMessage("Are you sure you want to leave this event?")
                    .setPositiveButton("Yes", (dialog, which) -> leaveEvent(event.getName(), position, holder.itemView.getContext()))
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    /**
     * Returns the total number of events on the waitlist.
     *
     * @return The size of the events list.
     */
    @Override
    public int getItemCount() {
        Log.d("AdapterDebug", "Item count: " + events.size());
        return events.size();
    }

    /**
     * Removes the entrant from the specified event's waitlist in Firestore, and displays a custom toast notification.
     *
     * @param eventName The name of the event to leave.
     * @param position  The position of the event in the RecyclerView.
     * @param context   The context for displaying the toast notification.
     */
    private void leaveEvent(String eventName, int position, Context context) {
        db.collection("events").document(eventName)
                .update("waitList", FieldValue.arrayRemove(entrantID))
                .addOnSuccessListener(aVoid -> {
                    events.remove(position);
                    notifyItemRemoved(position);

                    // Inflate custom toast layout
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View layout = inflater.inflate(R.layout.toast_notification_layout, null);

                    TextView toastText = layout.findViewById(R.id.toast_text);
                    toastText.setText("You've left the waiting list for this event.");

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

    /**
     * ViewHolder class that represents each event item on the waitlist.
     * Provides access to UI elements such as the event title and buttons for viewing details or leaving the event.
     */
    static class WaitlistViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle, eventDate, eventStart;

        Button viewDetailsButton, leaveEventButton;

        /**
         * Initializes the ViewHolder, finding the necessary UI elements for interaction.
         *
         * @param itemView The view representing each event item in the RecyclerView.
         */
        WaitlistViewHolder(View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.entrant_event_list_item_name);
            eventDate = itemView.findViewById(R.id.entrant_event_list_item_date);
            eventStart = itemView.findViewById(R.id.entrant_event_list_item_start);
            viewDetailsButton = itemView.findViewById(R.id.entrant_event_list_details_button);
            leaveEventButton = itemView.findViewById(R.id.entrant_event_list_leave_wait_button);
        }
    }
}
