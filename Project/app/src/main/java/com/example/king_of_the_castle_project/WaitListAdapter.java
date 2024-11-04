package com.example.king_of_the_castle_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class WaitListAdapter extends ArrayAdapter<Entrant> {
    public WaitListAdapter(@NonNull Context context, ArrayList<Entrant> entrants) {
        super(context, 0, entrants);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.organiser_event_waitlist_content,
                    parent, false);
        } else {
            view = convertView;
        }
        Entrant entrant = getItem(position);
        TextView entrantName = view.findViewById(R.id.entrant_name_LOE_screen);
        TextView entrantPhoneNumber = view.findViewById(R.id.entrant_phone_number_LOE_screen);

        entrantName.setText(entrant.getName());
        entrantPhoneNumber.setText(entrant.getPhoneNumber());
        return view;
    }
}
