package com.example.king_of_the_castle_project;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Console;
import java.util.ArrayList;

public class WaitListAdapter extends ArrayAdapter<String> {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public WaitListAdapter(@NonNull Context context, ArrayList<String> entrants) {
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
        String entrant = getItem(position);

        // Reset or bind view placeholders to avoid displaying stale data
        TextView entrantName = view.findViewById(R.id.entrant_name_LOE_screen);
        TextView entrantEmail = view.findViewById(R.id.entrant_email_LOE_screen);
        TextView entrantPhoneNumber = view.findViewById(R.id.entrant_phone_number_LOE_screen);

        entrantName.setText("Loading...");
        entrantEmail.setText("Loading...");
        entrantPhoneNumber.setText("Loading...");

        // Bind the position to the view's tag
        view.setTag(position);

        // Get entrants from the database
        getUser(entrant, position, view);
        notifyDataSetChanged();

        return view;
    }

    private void getUser(String entrant, int position, View view) {
        // query the database
        db.collection("entrants")
                .whereEqualTo("id", entrant)
                .get()
                .addOnCompleteListener(task ->  {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Check if the view is still bound to this position
                            if ((int) view.getTag() == position) {
                                // Populate the views with data
                                TextView entrantName = view.findViewById(R.id.entrant_name_LOE_screen);
                                TextView entrantEmail = view.findViewById(R.id.entrant_email_LOE_screen);
                                TextView entrantPhoneNumber = view.findViewById(R.id.entrant_phone_number_LOE_screen);

                                entrantName.setText(document.getString("name"));
                                entrantEmail.setText(document.getString("email"));
                                String phone = document.getString("phone");
                                if (phone != null && !phone.isEmpty()) {
                                    entrantPhoneNumber.setText(phone);
                                } else {
                                    entrantPhoneNumber.setText("No phone number provided");
                                }
                            }
                        }
                    } else {
                        // Handle error
                        Toast.makeText(getContext(), "Error fetching user info", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}