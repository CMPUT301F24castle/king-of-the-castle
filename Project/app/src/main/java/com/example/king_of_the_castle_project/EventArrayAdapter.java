package com.example.king_of_the_castle_project;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EventArrayAdapter extends ArrayAdapter<Event>  {
    public EventArrayAdapter(@NonNull Context context, List<Event> events) {
        super(context, 0, events);
    }

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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.organizer_event_list_content, parent, false);
        }
        // Image view for QR Code
        ImageView qrCodeImage = convertView.findViewById(R.id.organizer_event_qr_code);
        // Get views
        TextView name = convertView.findViewById(R.id.organizer_event_name);
        Button viewEntrantsButton = convertView.findViewById(R.id.view_entrants_button);
        Button sampleEntrantsButton = convertView.findViewById(R.id.sample_entrants_button);

        // Get QR Code
        if (event.getQrCodeData() != null) {
            byte[] decodedBytes = Base64.decode(event.getQrCodeData(), Base64.DEFAULT);
            Bitmap qrCodeBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            qrCodeImage.setImageBitmap(qrCodeBitmap);
        } else {
            Log.d("Failed to show QR code", "failure: " + event.getQrCodeData());
        }

        if (event != null) {
            // For now just name, add others later
            name.setText(event.getName());
        }

        viewEntrantsButton.setOnClickListener(v -> {
            // make it go to entrants screen
            Intent i = new Intent(getContext(), ListOfEntrantsInEventScreen.class);
            i.putExtra("Waitlist", event.getWaitList());
            getContext().startActivity(i);
        });

        sampleEntrantsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }
}
