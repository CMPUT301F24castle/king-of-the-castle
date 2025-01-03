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

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * ArrayAdapter to show list of events in the administrator browse
 */
public class EventAdminArrayAdapter extends ArrayAdapter<Event> {
    private final Context context;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

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
            ImageView eventPosterImage = convertView.findViewById(R.id.organizer_event_poster);
            // Get views
            TextView name = convertView.findViewById(R.id.organizer_event_name);
            Button removeEventButton = convertView.findViewById(R.id.remove_event_button);
            Button removeQrDataButton = convertView.findViewById(R.id.remove_qr_button);
            Button removeFacilityButton = convertView.findViewById(R.id.remove_facility_button);
            Button viewEventButton = convertView.findViewById(R.id.view_details_button);

            // reset image view
            eventPosterImage.setImageBitmap(null);

            // Get QR Code
            if (event.getQrCodeData() != null) {
                byte[] decodedBytes = Base64.decode(event.getQrCodeData(), Base64.DEFAULT);
                Bitmap qrCodeBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                qrCodeImage.setImageBitmap(qrCodeBitmap);
            } else {
                Log.d("Failed to show QR code", "failure: " + event.getQrCodeData());
            }

            // Set the image view
            String imageID = event.getHashIdentifier();
            eventPosterImage.setTag(imageID);
            if (imageID != null) {
                db.collection("images")
                        .document(imageID)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                if (imageID.equals(eventPosterImage.getTag())) {
                                    String conversion = documentSnapshot.getString("imageData");
                                    if (conversion != null) {
                                        byte[] decodedImage = Base64.decode(conversion, Base64.DEFAULT);
                                        Bitmap imageBitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
                                        eventPosterImage.setImageBitmap(imageBitmap);
                                    }
                                }
                            } else {
                                eventPosterImage.setImageResource(R.drawable.missing_image_icon);
                            }
                        });
            }

            if (event != null) {
                // For now just name, add others later
                name.setText(event.getName());
            }
            // Disable a QR code
            removeQrDataButton.setOnClickListener(v -> {
                String eventIdentifier = event.getHashIdentifier();
                // FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("events")
                        .document(eventIdentifier)
                        .update("qrCodeValid", false);
                Toast.makeText(this.context, "QR Code disabled", Toast.LENGTH_LONG);
                ((Activity) context).finish();
            });

            viewEventButton.setOnClickListener(v -> {
                // Create a Dialog
                android.app.Dialog dialog = new android.app.Dialog(context);
                dialog.setContentView(R.layout.event_details_dialogue);
                dialog.setTitle("Event Details");

                // Set dialog window properties
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setGravity(Gravity.CENTER);

                // Put the event data into the layout
                TextView nameTextView = dialog.findViewById(R.id.name_textview);
                TextView dateTextView = dialog.findViewById(R.id.date_textview);
                TextView timeTextView = dialog.findViewById(R.id.time_textview);
                TextView venueTextView = dialog.findViewById(R.id.venue_textview);
                TextView organizerNotesTextView = dialog.findViewById(R.id.organizer_notes_textview);
                TextView maxParticipantsTextView = dialog.findViewById(R.id.max_participants_textview);

                // Put info into the dialogue
                nameTextView.setText(event.getName());
                dateTextView.setText(event.getDate());
                timeTextView.setText(event.getTime());
                venueTextView.setText(event.getLocation());
                organizerNotesTextView.setText(event.getEventDetails());
                maxParticipantsTextView.setText(String.valueOf(event.getMaxParticipants()));

                // Display the dialog
                dialog.show();
            });


            // Remove an event
            removeEventButton.setOnClickListener(v -> {
                String eventToRemove = event.getHashIdentifier();

                Log.d("RemoveEvent", "Hash Identifier: " + eventToRemove);

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // delete from database
                try {
                    db.collection("events").document(eventToRemove)
                            .delete();
                    ((Activity) context).finish();
                } catch (Exception e) {
                    Log.d("Error: ", "Problem: " + e);
                    ((Activity) context).finish();
                }
            });

            removeFacilityButton.setOnClickListener(v -> {
                String organizerID = event.getOrganizerID();
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                try {
                    db.collection("facilities").document(organizerID)
                            .delete();
                } catch (Exception e) {
                    Log.d("Error: ", "Problem: " + e);
                }

                try {
                    db.collection("events")
                            .whereEqualTo("organizerID", organizerID)
                            .get()
                            .addOnCompleteListener(task -> {
                                for (DocumentSnapshot document : task.getResult()) {
                                    db.collection("events")
                                            .document(document.getId())
                                            .delete();
                                }
                            });
                    ((Activity) context).finish();
                } catch (Exception e) {
                    Log.d("Error: ", "Problem: " + e);
                }

            });

            return convertView;
        }
}