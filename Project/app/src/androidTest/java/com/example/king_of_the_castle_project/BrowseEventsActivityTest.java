package com.example.king_of_the_castle_project;

import android.widget.Button;

import androidx.test.core.app.ActivityScenario;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrowseEventsActivityTest {

    private BrowseEventsActivity activity;

    @Before
    public void setUp() {
        // Launch the activity
        ActivityScenario.launch(BrowseEventsActivity.class);
    }

    @Test
    public void testActivityLaunch() {
        // Check if the activity has launched correctly
        ActivityScenario.launch(BrowseEventsActivity.class).onActivity(activity -> {
            assertNotNull(activity.findViewById(R.id.event_list));  // Check if ListView is present
            assertNotNull(activity.findViewById(R.id.return_button));  // Check if return button is present
        });
    }

    @Test
    public void testReturnButtonClick() {
        // Test if return will return to the previous screen/call finish
        ActivityScenario.launch(BrowseEventsActivity.class).onActivity(activity -> {
            activity.findViewById(R.id.return_button).performClick();
            assertTrue(activity.isFinishing());
        });
    }
}