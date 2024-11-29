package com.example.king_of_the_castle_project;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.app.AlertDialog;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

/*
 * ArrayAdapter used to format the eventlist in ManageEventsActivity
 */
public class EventArrayAdapter extends ArrayAdapter<Event>  {
    public EventArrayAdapter(@NonNull Context context, List<Event> events) {
        super(context, 0, events);

    }

    //ANGELA TESTING VARIABLES//
    private ArrayList<String> testwaitlist;
    private Lottery testlottery;
    private Event testevent;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        ImageView eventPosterImage = convertView.findViewById(R.id.organizer_event_poster);
        // Get views
        TextView name = convertView.findViewById(R.id.organizer_event_name);
        Button viewEntrantsButton = convertView.findViewById(R.id.view_entrants_button);
        Button sampleEntrantsButton = convertView.findViewById(R.id.sample_entrants_button);
        Button sendNotificationsButton = convertView.findViewById(R.id.send_notifications_button);

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
        if (imageID != null) {
            db.collection("images")
                    .document(imageID)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String conversion = documentSnapshot.getString("imageData");
                            if (conversion != null) {
                                byte[] decodedImage = Base64.decode(conversion, Base64.DEFAULT);
                                Bitmap imageBitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
                                eventPosterImage.setImageBitmap(imageBitmap);
                            }
                        }
                    });
        }

        if (event != null) {
            // For now just name, add others later
            name.setText(event.getName());
        }

        viewEntrantsButton.setOnClickListener(v -> {
            // get context
            Context context = v.getContext();

            // inflate dialog layout
            AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
            View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.organizer_entrant_list_type_dialogue_style, null);
            builder.setView(dialogView);

            // get views in dialog
            AppCompatButton waitlistButton = dialogView.findViewById(R.id.view_waitlist_entrants_button);
            AppCompatButton invitedButton = dialogView.findViewById(R.id.view_invited_entrants_button);
            AppCompatButton cancelledButton = dialogView.findViewById(R.id.view_cancelled_entrants_button);
            AppCompatButton enrolledButton = dialogView.findViewById(R.id.view_enrolled_entrants_button);
            TextView cancelTextView = dialogView.findViewById(R.id.choose_entrant_type_cancel);

            // create and show dialog
            AlertDialog dialog = builder.create();
            dialog.show();

            // set on click listeners
            // handle view waitlist entrants
            waitlistButton.setOnClickListener(view -> {
                // dismiss dialog
                dialog.dismiss();
                // change screen
                Intent i = new Intent(context, ListOfFilteredEntrantsInEventScreen.class);
                i.putExtra("entrant_id_list", event.getWaitList());
                context.startActivity(i);
            });

            // handle view invited entrants
            invitedButton.setOnClickListener(view -> {
                // dismiss dialog
                dialog.dismiss();
                // change screen
                Intent i = new Intent(context, ListOfFilteredEntrantsInEventScreen.class);
                i.putExtra("entrant_id_list", event.getAcceptedList());
                //ArrayList<String> acceptedList = new ArrayList<>(Arrays.asList("21cd01a5f09d6e83"));
                //i.putExtra("entrant_id_list",acceptedList);
                context.startActivity(i);
            });

            // handle view cancelled entrants
            cancelledButton.setOnClickListener(view -> {
                // dismiss dialog
                dialog.dismiss();
                // change screen
                Intent i = new Intent(context, ListOfFilteredEntrantsInEventScreen.class);
//                i.putExtra("entrant_id_list", event.getDeclinedList());
                ArrayList<String> declinedList = new ArrayList<>(Arrays.asList("80f328806d47ddbb"));
                i.putExtra("entrant_id_list",declinedList);
                context.startActivity(i);
            });

            // handle view enrolled entrants
            enrolledButton.setOnClickListener(view -> {
                // dismiss dialog
                dialog.dismiss();
                // change screen
                Intent i = new Intent(context, ListOfFilteredEntrantsInEventScreen.class);
//                i.putExtra("entrant_id_list", event.getRegisteredList());
                ArrayList<String> registeredList = new ArrayList<>(Arrays.asList("2b075d2817445df8", "cc10cc7754d9fe8a"));
                i.putExtra("entrant_id_list",registeredList);
                context.startActivity(i);
            });

            cancelTextView.setOnClickListener(view -> {
                // dismiss dialog
                dialog.dismiss();
            });
        });


        sampleEntrantsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sendNotificationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // inflate dialog layout
                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.notify_entrants_dialog, null);
                builder.setView(dialogView);

                // get views inside dialog
                EditText messageEditText = dialogView.findViewById(R.id.sample_entrants_edittext);
                Spinner roleSpinner = dialogView.findViewById(R.id.choose_role_spinner);
                Button okButton = dialogView.findViewById(R.id.notify_entrants_dialogue_button);

                // allow edit text to be editable
                messageEditText.setFocusableInTouchMode(true);

                // create and show dialog
                AlertDialog dialog = builder.create();
                dialog.show();

                // setup spinner
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                        v.getContext(),
                        R.array.notify_entrants_spinner,
                        android.R.layout.simple_spinner_item
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                roleSpinner.setAdapter(adapter);






                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // message to send/notify with
                        String message = messageEditText.getText().toString();
                        // type of entrant to notify in string (Waitlist Entrants, Cancelled Entrants, Invited Entrants, Enrolled Entrants)
                        String selectedRole = roleSpinner.getSelectedItem().toString();

                        dialog.dismiss();
                    }
                });
            }
        });

        return convertView;
    }
}
