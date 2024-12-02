package com.example.king_of_the_castle_project;

import static android.app.Activity.RESULT_OK;
import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.app.Activity;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.app.AlertDialog;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * ArrayAdapter used to format the event list in ManageEventsActivity.
 * Includes functionality for interacting with Firebase and displaying events.
 */
public class EventArrayAdapter extends ArrayAdapter<Event>  {

    private FirebaseFirestore db;
    private Lottery lottery;

    private ImageSelection listener;

    // Image selection interface for parent activity
    public interface ImageSelection {
        void onImageSelectionRequested(String eventHashIdentifier);
    }

    public EventArrayAdapter(@NonNull Context context, List<Event> events, ImageSelection listener) {
        super(context, 0, events);
        this.listener = listener;
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
     *      The layout for which the array item will be displayed
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get event
        Event event = getItem(position);
        lottery = new Lottery();


        //get firebase
        db = FirebaseFirestore.getInstance();
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
        Button editEvent = convertView.findViewById(R.id.edit_event_button);

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
                            eventPosterImage.setImageResource(R.drawable.baseline_person_24_black);
                        }
                    });
        }

        if (event != null) {
            // For now just name, add others later
            name.setText(event.getName());
            editEvent.setTag(event.getHashIdentifier());
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
                i.putExtra("event_id", event.getHashIdentifier());
                i.putExtra("event_geolocation_toggle", event.getGeolocation());
                i.putExtra("entrant_list_type", "waitList");
                context.startActivity(i);
            });

            // handle view invited entrants
            invitedButton.setOnClickListener(view -> {
                // dismiss dialog
                dialog.dismiss();
                // change screen
                Intent i = new Intent(context, ListOfFilteredEntrantsInEventScreen.class);
                i.putExtra("entrant_id_list", event.getAcceptedList());
                i.putExtra("event_id", event.getHashIdentifier());
                i.putExtra("event_geolocation_toggle", event.getGeolocation());
                i.putExtra("entrant_list_type", "acceptedList");
                context.startActivity(i);
            });

            // handle view cancelled entrants
            cancelledButton.setOnClickListener(view -> {
                // dismiss dialog
                dialog.dismiss();
                // change screen
                Intent i = new Intent(context, ListOfFilteredEntrantsInEventScreen.class);
                i.putExtra("entrant_id_list", event.getDeclinedList());
                i.putExtra("event_id", event.getHashIdentifier());
                i.putExtra("event_geolocation_toggle", event.getGeolocation());
                i.putExtra("entrant_list_type", "declinedList");
                context.startActivity(i);
            });

            // handle view enrolled entrants
            enrolledButton.setOnClickListener(view -> {
                // dismiss dialog
                dialog.dismiss();
                // change screen
                Intent i = new Intent(context, ListOfFilteredEntrantsInEventScreen.class);
                i.putExtra("entrant_id_list", event.getRegisteredList());
                i.putExtra("event_id", event.getHashIdentifier());
                i.putExtra("event_geolocation_toggle", event.getGeolocation());
                i.putExtra("entrant_list_type", "registeredList");
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
                // inflate dialog layout
                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.sample_entrants_dialogue_style, null);
                builder.setView(dialogView);

                //get views inside dialog
                EditText messageEditText = dialogView.findViewById(R.id.sample_entrants_edittext);
                Button okButton = dialogView.findViewById(R.id.sample_entrants_dialogue_button);

                //allow edit text to be editable
                messageEditText.setFocusableInTouchMode(true);

                // create and show dialog
                AlertDialog dialog = builder.create();
                dialog.show();

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // message to send/notify with
                        String message = messageEditText.getText().toString();
                        int number = Integer.parseInt(message);

                        //lottery = new Lottery();

                        lottery.selectRandomEntrants(event, number);
                        List selectedAttendees = lottery.getSelectedAttendees();


                        DocumentReference ref = db.collection("events").document(event.getHashIdentifier());
                        for (int i = 0; i < selectedAttendees.size(); i++) {
                            ref.update("acceptedList", FieldValue.arrayUnion(selectedAttendees.get(i)));
                        }
                        //Log.d("Test", "Error" + FieldValue.arrayUnion(selectedAttendees));
                        //ref.update("acceptedList", FieldValue.arrayUnion(selectedAttendees));


                       // event.getHashIdentifier();


                        dialog.dismiss();
                    }
                });





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

                        //notification functionality
                        // Firebase reference to the event's document
                       // DocumentReference eventRef = db.collection("events").document(event.getHashIdentifier());


                        //ORIGINAL IMPLEMENTATION
                        //if lottery is null
                        if (lottery == null) {
                            Toast.makeText(v.getContext(), "No attendees were selected.", Toast.LENGTH_LONG).show();
                        }

                        else {
                            notifyLottery notificationSender = new notifyLottery(lottery);
                            notificationSender.onClick(v);


                            Toast.makeText(v.getContext(), "Notifications sent successfully!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                        dialog.dismiss();
                    }
                });
            }
        });

        editEvent.setOnClickListener(v -> {
            String eventHashIdentifier = (String) v.getTag();
            listener.onImageSelectionRequested(eventHashIdentifier);
        });

        return convertView;
    }
}
