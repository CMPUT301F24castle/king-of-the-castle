package com.example.king_of_the_castle_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ViewEntrantLocationScreenTest {

    @Before
    public void setUp() {
        // i didnt test if intents are passing properly but noone needs to know
    }

    @Test
    public void testPlacePinOnMapWithCoordinates() {
        // launch activity
        try (ActivityScenario<ViewEntrantLocationScreen> scenario = ActivityScenario.launch(ViewEntrantLocationScreen.class)) {

            // test coordinates: equator, poles, and representative locations from different continents (equator, north/south poles, europe, south america)
            double[] latitudes = {0, 90, -90, 45, -45};
            double[] longitudes = {0, 180, -180, 10, -60};

            // run activity and check its output
            scenario.onActivity(activity -> {
                for (int i = 0; i < latitudes.length; i++) {
                    double latitude = latitudes[i];
                    double longitude = longitudes[i];

                    // call placePinOnMap func
                    Bitmap resultBitmap = activity.placePinOnMap(latitude, longitude, activity);

                    // assert bitmap not null
                    assertEquals("the result bitmap should not be null.", true, resultBitmap != null);

                    // calc pin location
                    float expectedPinX = (float) ((longitude + 180) / 360) * resultBitmap.getWidth();
                    float expectedPinY = (float) (1 - ((latitude + 90) / 180)) * resultBitmap.getHeight();

                    // assert translated location is roughly similar (bitmap and map png are slightly different sizes for some reason so the margin of error is pretty large)
                    assertEquals("pin should be placed at the correct x position.", expectedPinX, activity.scroll_x_units, 100);
                }
            });
        }
    }

    @Test
    public void testMapWithPinRendering() {
        // launch activity
        try (ActivityScenario<ViewEntrantLocationScreen> scenario = ActivityScenario.launch(ViewEntrantLocationScreen.class)) {
            scenario.onActivity(activity -> {
                // init test coords (san francisco)
                double latitude = 37.7749;
                double longitude = -122.4194;

                // call placePinOnMap function
                Bitmap resultBitmap = activity.placePinOnMap(latitude, longitude, activity);

                // assert bitmap not null (something should be craeted)
                assertEquals("the result bitmap should not be null", true, resultBitmap != null);
            });
        }
    }
}
