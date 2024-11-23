package com.example.king_of_the_castle_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;

/**
 * This activity displays a map and a marker indicating a specific location based on latitude and longitude provided via intents.
 */
public class ViewEntrantLocationScreen extends AppCompatActivity {

    private double latitude;
    private double longitude;
    private float scroll_x_units;

    /**
     * Default method for basic startup logic
     * @param savedInstanceState
     *      If there was an Instance saved, saved instances restores it
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrant_map_location);

        // get views
        ImageView mapImageView = findViewById(R.id.mapView);
        Button returnButton = findViewById(R.id.return_button);
        HorizontalScrollView horizontalScrollView = findViewById(R.id.scrollView);

        // unpack coordinates from intents
        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);

        // create bitmap of pin on map
        Bitmap resultBitmap = placePinOnMap(latitude, longitude, ViewEntrantLocationScreen.this);

        // set mapview to bitmap
        mapImageView.setImageBitmap(resultBitmap);

        // scroll to location
        horizontalScrollView.post(() -> horizontalScrollView.scrollTo((int)scroll_x_units, 0));

        // on click listener for return
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // finish map activity and return to prev
                finish();
            }
        });
    }

    public Bitmap placePinOnMap(double latitude, double longitude, Context context) {
        // load pngs as bitmaps
        Bitmap map = BitmapFactory.decodeResource(context.getResources(), R.drawable.equirectangular_map);
        Bitmap pin = BitmapFactory.decodeResource(context.getResources(), R.drawable.png_location_pin);

        // create bitmap object with same size as map
        Bitmap resultBitmap = Bitmap.createBitmap(map.getWidth(), map.getHeight(), Bitmap.Config.ARGB_8888);

        // create canvas
        Canvas canvas = new Canvas(resultBitmap);

        // draw map on canvas
        canvas.drawBitmap(map, 0, 0, null);

        // get map size
        int mapWidth = map.getWidth();
        int mapHeight = map.getHeight();

        // convert from latitude and longitude to pixel coordinates
        float x = (float) ((longitude + 180)/360) * mapWidth;
        float y = (float) (1-((latitude+90)/180))* mapHeight;

        // adjust the pin position to center it at (x, y)
        float pinX = x - (pin.getWidth() / 2); // center horizontally
        float pinY = y - (pin.getHeight()); // bottom of pin points to location

        scroll_x_units = pinX;

        // draw pin on map/canvas at the calculated position
        canvas.drawBitmap(pin, pinX, pinY, null);

        // return resulting map+pin
        return resultBitmap;
    }


}
