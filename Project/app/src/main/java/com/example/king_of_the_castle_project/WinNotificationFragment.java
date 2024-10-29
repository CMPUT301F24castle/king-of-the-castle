package com.example.king_of_the_castle_project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class WinNotificationFragment extends DialogFragment {
    private WinNotificationFragment.WinNotificationDialogListener listener;
    private Toast toastMessage;
    //private Event event;

    interface WinNotificationDialogListener {
        // Defines functions to be implemented in MainActivity
    }

    @Override
    public void onAttach(@NonNull Context context) {
        // Defining listener
        super.onAttach(context);
        if (context instanceof WinNotificationFragment.WinNotificationDialogListener) {
            listener = (WinNotificationFragment.WinNotificationDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement WinNotificationDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Setting layout of the fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.win_notification_fragment, null);

        TextView winNotificationTextview = view.findViewById(R.id.win_notification_textview);
        TextView dateTextview = view.findViewById(R.id.date_textview);
        TextView timeTextview = view.findViewById(R.id.time_textview);
        TextView venueTextview = view.findViewById(R.id.venue_textview);
        TextView addressTextview = view.findViewById(R.id.address_textview);
        TextView notesTextview = view.findViewById(R.id.organizer_notes_textview);
        TextView maxTextview = view.findViewById(R.id.max_participants_textview);
        Button acceptButton = view.findViewById(R.id.accept_invite_button);
        Button declineButton = view.findViewById(R.id.decline_invite_button);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastMessage = Toast.makeText(getActivity(), "You have accepted the invitation", Toast.LENGTH_SHORT);
                toastMessage.show();
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastMessage = Toast.makeText(getActivity(), "You have declined the invitation", Toast.LENGTH_SHORT);
                toastMessage.show();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setView(view)
                .setTitle("Lottery Result")
                .setNegativeButton("Exit", null)
                .create();
    }



}
