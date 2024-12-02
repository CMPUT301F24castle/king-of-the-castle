package com.example.king_of_the_castle_project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.Gravity;
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

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * ArrayAdapter used to format the entrant list in BrowseProfilesActivity
 */
public class ProfileAdminArrayAdapter extends ArrayAdapter<Entrant> {
    private Context context;
    private ArrayList<Entrant> entrants;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ProfileAdminArrayAdapter(@NonNull Context context, ArrayList<Entrant> entrants) {
        super(context, R.layout.admin_profile_browsing_screen_list_content, entrants);
        this.context = context;
        this.entrants = entrants;
    }

    /**
     * Gets the view of the adapter
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return
     */
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
                if (entrant.getName() != null && !entrant.getName().isEmpty()) {
                    Bitmap fallbackBitmap = createDeterministicPFP(entrant.getName().charAt(0));
                    userPfp.setImageBitmap(fallbackBitmap);
                } else {
                    userPfp.setImageResource(R.drawable.baseline_person_24_black);
                }
            }
        }

        // set onclicks for buttons
        // remove entrants pfp from db
        removeImageButton.setOnClickListener(v -> {
            // check if pfp is there, if it is remove, if it isn't toast
            if (entrant.getPfpData() == null){
                // inflate layout
                LayoutInflater inflater = LayoutInflater.from(context);
                View layout = inflater.inflate(R.layout.toast_notification_layout, null);

                // set msg
                TextView text = layout.findViewById(R.id.toast_text);
                text.setText("No image data to remove.");

                // show toast
                Toast toast = new Toast(context);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.setGravity(Gravity.BOTTOM, 0, 100);
                toast.show();
            } else {
                db.collection("entrants").document(entrant.getId())
                        .update("profileImg", null)
                        .addOnSuccessListener(aVoid -> {
                            // remove from entrant
                            entrant.setPfpData(null);
                            // set deterministic pfp
                            userPfp.setImageResource(R.drawable.baseline_person_24_black);
                        });
            }
        });

        // remove entrant from db
        removeProfileButton.setOnClickListener(v -> {
            if (entrant != null) {
                // get entrant id
                String entrantId = entrant.getId();

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

    /**
     * Creates a deterministic profile picture bitmap with the first letter of the name.
     *
     * @param firstLetter The first letter of the entrant's name.
     * @return A Bitmap representing the fallback profile picture.
     */
    private Bitmap createDeterministicPFP(char firstLetter) {
        // set pfp size, color, bg color
        int size = 100;
        int textSize = 50;
        int bgColor = 0xFFB5D1A2; // light green but in hex
        int textColor = 0xFF000000; // black

        // init blank bitmap
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        // init canvas to draw on bitmap
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(bgColor);

        // init paint
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.CENTER);

        // draw on canvas
        float x = size / 2f;
        float y = size / 2f - ((paint.descent() + paint.ascent()) / 2f);
        canvas.drawText(String.valueOf(firstLetter).toUpperCase(), x, y, paint);

        return bitmap;
    }
}
