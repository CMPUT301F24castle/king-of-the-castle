package com.example.king_of_the_castle_project;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Base64;
import android.util.Log;
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
import java.util.Map;

/**
 * Custom ArrayAdapter for displaying a list of entrants along with their geographic location data.
 * Each item in the list contains entrant information (name, phone number) and their location
 * (latitude and longitude). The adapter also includes a button that, when clicked, launches a
 * map screen showing the entrant's location on a map.
 */
public class EntrantListArrayAdapterWithLocation extends ArrayAdapter<Map<String, Object>> {
    /**
     * Constructs a new EntrantListArrayAdapterWithLocation.
     *
     * @param context The context in which the adapter is used, typically the Activity or Fragment.
     * @param entrantLocationGeoList The list of maps, each containing data about an entrant and their location (latitude and longitude).
     */
    public EntrantListArrayAdapterWithLocation(Context context, ArrayList<Map<String, Object>> entrantLocationGeoList) {
        super(context, 0, entrantLocationGeoList);
    }

    /**
     * Returns a view for a given entrant item in the list, including their information and a button
     * to view the entrant's location on a map. This method inflates the layout, binds the entrant's
     * data (name, phone number, location), and sets the functionality for the location button.
     *
     * @param position The position of the item within the list data.
     * @param convertView A recycled view that can be reused, or null if no view is available.
     * @param parent The parent ViewGroup that the view will be attached to.
     * @return The View representing the list item for the given position, including entrant info and a location button.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get the current map object
        Map<String, Object> entrantData = getItem(position);
        // inflate the view
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.organizer_entrant_list_content, parent, false);
        } else {
            view = convertView;
        }
        // get the views for displaying information
        ImageView entrantPFP = view.findViewById(R.id.entrant_pfp);
        TextView entrantInfo = view.findViewById(R.id.entrant_info);
        AppCompatButton viewEntrantLocationButton = view.findViewById(R.id.view_entrant_location_button);
        // extract data from the map
        Entrant entrant = (Entrant) entrantData.get("entrant");
        double latitude = (double) entrantData.get("latitude");
        double longitude = (double) entrantData.get("longitude");

        // create a string to display entrant information
        String entrant_name = entrant.getName();
        String entrant_phoneNum = entrant.getPhoneNumber();
        String entrant_info_text = "Name: " + entrant_name + "\nPN: " + entrant_phoneNum;

        // set the text view for entrant info
        entrantInfo.setText(entrant_info_text);

        // decode and set the user's profile picture
        String base64PFP = entrant.getPfpData();
        Bitmap pfpBitmap = getPFP(base64PFP);
        if (pfpBitmap != null) {
            entrantPFP.setImageBitmap(pfpBitmap);
        } else {
            // set deterministic pfp
            if (entrant_name != null && !entrant_name.isEmpty()) {
                Bitmap fallbackBitmap = createDeterministicPFP(entrant_name.charAt(0));
                entrantPFP.setImageBitmap(fallbackBitmap);
            } else {
                entrantPFP.setImageResource(R.drawable.baseline_person_24_black);
            }
        }

        // set on click listener for the location button
        viewEntrantLocationButton.setOnClickListener(v -> {
            // get context
            Context context = v.getContext();
            // get map screen
            Intent i = new Intent(context, ViewEntrantLocationScreen.class);
            // send latitude and longitude
            i.putExtra("latitude", latitude);
            i.putExtra("longitude", longitude);
            // start map screen
            context.startActivity(i);
        });
        // return the view to be displayed
        return view;
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