package com.example.king_of_the_castle_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * ArrayAdapter used to format the entrant list in BrowseProfilesActivity
 */
public class ProfileAdminArrayAdapter extends ArrayAdapter<Entrant> {
    private Context context;
    private ArrayList<Entrant> entrants;

    public ProfileAdminArrayAdapter(@NonNull Context context, ArrayList<Entrant> entrants) {
        super(context, R.layout.admin_profile_browsing_screen_list_content, entrants);
        this.context = context;
        this.entrants = entrants;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.admin_profile_browsing_screen_list_content, parent, false);
        }

        Entrant entrant = getItem(position);

        // get views
        ImageView userPfp = convertView.findViewById(R.id.user_pfp);
        TextView userInfo = convertView.findViewById(R.id.user_info);
        AppCompatButton removeImageButton = convertView.findViewById(R.id.remove_image_button);
        AppCompatButton removeProfileButton = convertView.findViewById(R.id.remove_profile_button);

        // set up user info
        if (entrant != null) {
            // turn entrants info into one text block
            String tempString = entrant.getName() + "\n" + "Email: " + entrant.getEmail() + "\n" + "Phone number: " + entrant.getPhoneNumber() + "\n" + "Device ID: " + entrant.getId();
            userInfo.setText(tempString);

            // decode and set the user's profile picture
            String base64PFP = entrant.getPfpData();
            Bitmap pfpBitmap = getPFP(base64PFP);
            if (pfpBitmap != null) {
                userPfp.setImageBitmap(pfpBitmap);
            } else {
                // set deterministic pfp
                userPfp.setImageResource(R.drawable.baseline_person_24_black);
            }
        }

        // set onclicks for buttons
        // remove entarnts pfp from db
        removeImageButton.setOnClickListener(v -> {
            // implement when pfps are ready
        });

        // remove entrant from db
        removeProfileButton.setOnClickListener(v -> {
            if (entrant != null) {
                // get entrant id
                String entrantId = entrant.getId();

                // get db
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // query and remove entrant based on id
                db.collection("entrants").document(entrantId)
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            // remove from entrant list
                            entrants.remove(position);
                            // notify array adapter
                            notifyDataSetChanged();
                        });
            }
        });

        return convertView;
    }

    /**
     * Decodes a Base64-encoded string representing a profile picture (PFP) and returns it as a Bitmap.
     * If the provided Base64 string is null, the method logs a failure message and returns null.
     *
     * @param base64PFP A Base64-encoded string representing the user's profile picture.
     *                  This string should be a valid encoded image format.
     * @return A Bitmap object representing the decoded profile picture, or null if the Base64 string is null.
     */
    public Bitmap getPFP(String base64PFP) {
        if (base64PFP != null) {
            byte[] decodedBytes = Base64.decode(base64PFP, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } else {
            Log.d("Failed to show PFP", "failure: " + base64PFP);
            return null;
        }
    }
}
