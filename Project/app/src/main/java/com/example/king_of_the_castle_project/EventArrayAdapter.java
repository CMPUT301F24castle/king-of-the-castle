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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;

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
        Button sendNotificationsButton = convertView.findViewById(R.id.send_notifications_button);

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
                Button selectSpecificButton = dialogView.findViewById(R.id.notify_entrants_dialog_select_specific_button);
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

                // add on click listeners for buttons
                selectSpecificButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), "Not yet implemented", Toast.LENGTH_SHORT).show();
                    }
                });



                // ANGELA TEST NOTIFICATIONS HERE
                testwaitlist = new ArrayList<String>();
                // Create some entrants
                Entrant entrant1 = new Entrant("Alice", "alice@example.com", "555-1234", "id1");
                Entrant entrant2 = new Entrant("Bob", "bob@example.com", "555-5678", "id2");
                Entrant entrant3 = new Entrant("carl", "carl@example.com", "555-5788", "id3");

                // Add entrants to the waitlist
                testwaitlist.add(entrant1.getId());
                testwaitlist.add(entrant2.getId());
                testwaitlist.add(entrant3.getId());
                testevent = new Event("Castle Tour", "2024-11-15", "10:00 AM", "Castle Grounds",
                        "A guided tour of the castle", 2, testwaitlist, true);
                testlottery = new Lottery();
                testlottery.selectRandomEntrants(testevent);


                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // message to send/notify with
                        String message = messageEditText.getText().toString();
                        // type of entrant to notify in string (Waitlist Entrants, Cancelled Entrants, Invited Entrants, Enrolled Entrants)
                        String selectedRole = roleSpinner.getSelectedItem().toString();

                        //ANGELA TEST//
                        Notif2_5_1 notifier = new Notif2_5_1(v.getContext());
                        notifier.sendLotteryNotification(testlottery);



                        dialog.dismiss();
                    }
                });
            }
        });

        return convertView;
    }
}
