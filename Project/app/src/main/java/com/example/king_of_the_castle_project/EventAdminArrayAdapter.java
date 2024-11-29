package com.example.king_of_the_castle_project;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/*
 * ArrayAdapter to show list of events in the administrator browse
 */
public class EventAdminArrayAdapter extends ArrayAdapter<Event> {
    private final Context context;
    /**
     * Array adapter constructor
     * @param context
     *      Context for which the array adapter is being called
     * @param events
     *      List that it takes in
     */
    public EventAdminArrayAdapter(@NonNull Context context, List<Event> events) {
            super(context, 0, events);
            this.context = context;
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
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.administrator_event_list_content, parent, false);
            }
            // Image view for QR Code
            ImageView qrCodeImage = convertView.findViewById(R.id.organizer_event_qr_code);
            // Get views
            TextView name = convertView.findViewById(R.id.organizer_event_name);
            Button removeEventButton = convertView.findViewById(R.id.remove_event_button);
            Button removeFacilityButton = convertView.findViewById(R.id.remove_facility_button);

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




            removeFacilityButton.setOnClickListener(v -> {
                CharSequence text = "Facility removed due to violations of app policy!";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();




            });

            removeEventButton.setOnClickListener(v -> {


                String organizerID = event.getOrganizerID();
                String eventToRemove = event.getHashIdentifier();

                Log.d("RemoveEvent", "Hash Identifier: " + eventToRemove);

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // delete from database
                try {
                    db.collection("events").document(eventToRemove)
                            .delete();
                    db.collection(organizerID).document(eventToRemove)
                            .delete();
                    ((Activity) context).finish();
                } catch (Exception e) {
                    Log.d("This is fucking stupid", "Fuck:" + event.getHashIdentifier());
                    Log.d("Does the other one work", "Shit:" + event.getOrganizerID());
                    ((Activity) context).finish();
                }
            });

            return convertView;
        }
}