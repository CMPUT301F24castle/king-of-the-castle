package com.example.king_of_the_castle_project;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;
import org.junit.Test;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;

import android.widget.Button;

public class ManageFacilityActivityTest {
    @Test
    public void testReturnButton() {
        // Launch the activity
        ActivityScenario<ManageFacilityActivity> scenario = ActivityScenario.launch(ManageFacilityActivity.class);
        scenario.onActivity(activity -> {
            Button returnButton = activity.findViewById(R.id.return_button);

            // click return button
            returnButton.performClick();

            // finish activity
            assertTrue(activity.isFinishing());
        });
    }
}