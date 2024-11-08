package com.example.king_of_the_castle_project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NotificationLossArrayAdapter extends ArrayAdapter<Event> {
    public NotificationLossArrayAdapter(@NonNull Context context, List<Event> events) {
        super(context, 0, events);
        this.context = context;
    }
    private Context context;

    /**
     * Sets the view for an item in the list
     * @param position
     *      Position of the event in the dataset
     * @param convertView
     *      Old view to reuse if it exists, else inflate a new one
     * @param parent
     *      Parent view that the view will be attached to
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get event
        Event event = getItem(position);
        // Inflate the view if not reused
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.entrant_notification_list_content, parent, false);
        }
        // Get views
        TextView name = convertView.findViewById(R.id.notification_event_name);
        Button viewStatusButton = convertView.findViewById(R.id.view_status_button);


        if (event != null) {
            // For now just name, add others later
            name.setText(event.getName());
        }

        viewStatusButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, LossNotificationActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("event", event.getQrCodeData());
            intent.putExtra("name", event.getName());
            intent.putExtra("date", event.getDate());
            intent.putExtra("time", event.getTime());
            intent.putExtra("location", event.getLocation());
            intent.putExtra("notes", event.getEventDetails());
            intent.putExtra("max", event.getMaxParticipants());
            context.startActivity(intent);
            this.notifyDataSetChanged();
        });


        return convertView;
    }
}
