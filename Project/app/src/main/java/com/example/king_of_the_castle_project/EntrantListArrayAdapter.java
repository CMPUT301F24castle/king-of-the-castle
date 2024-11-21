package com.example.king_of_the_castle_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;

public class EntrantListArrayAdapter extends ArrayAdapter<Entrant>{
    public EntrantListArrayAdapter(Context context, ArrayList<Entrant> entrants){
        super(context, 0, entrants);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get item
        Entrant entrant = getItem(position);

        // inflate view
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.organizer_entrant_list_content, parent, false);
        } else {
            view = convertView;
        }

        // get views
        ImageView entrantPFP = view.findViewById(R.id.entrant_pfp);
        TextView entrantInfo = view.findViewById(R.id.entrant_info);
        AppCompatButton viewEntrantLocationButton = view.findViewById(R.id.view_entrant_location_button);

        // convert entrant object into usable data
        String entrant_name = entrant.getName();
        String entrant_phoneNum = entrant.getPhoneNumber();
        String entrant_info_text = "Name: " + entrant_name + "\nPN: " + entrant_phoneNum;


        // set views
//        entrantPFP.setImageResource(entrant.getPFP()); IMPLEMENT LATER


        entrantInfo.setText(entrant_info_text);

        // set on click for location button
        viewEntrantLocationButton.setOnClickListener(v -> {
            // view entrants location

        });

        // return
        return view;
    }
}
