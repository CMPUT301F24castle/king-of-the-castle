package com.example.king_of_the_castle_project;

import android.content.Context;
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

public class WaitingListAdapter extends RecyclerView.Adapter<WaitingListAdapter.ViewHolder> {
    private List<Event> eventList;
    private String entrantID; // The entrant's unique ID
    private FirebaseFirestore db;
    private Context context;

    public WaitingListAdapter(Context context, List<Event> eventList, String entrantID) {
        this.context = context;
        this.eventList = eventList;
        this.entrantID = entrantID;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_waitlist_pending, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.eventTitle.setText(event.getName());

        holder.viewDetailsButton.setOnClickListener(v -> {
            // Intent to event details can be implemented here
        });

        holder.leaveEventButton.setOnClickListener(v -> leaveEvent(event.getId(), holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    private void leaveEvent(String eventID, int position) {
        db.collection("events").document(eventID)
                .update("waitingList", FieldValue.arrayRemove(entrantID))
                .addOnSuccessListener(aVoid -> {
                    eventList.remove(position);
                    notifyItemRemoved(position);
                });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle;
        Button viewDetailsButton, leaveEventButton;

        public ViewHolder(View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.event_title);
            viewDetailsButton = itemView.findViewById(R.id.view_details_button);
            leaveEventButton = itemView.findViewById(R.id.leave_event_button);
        }
    }
}
