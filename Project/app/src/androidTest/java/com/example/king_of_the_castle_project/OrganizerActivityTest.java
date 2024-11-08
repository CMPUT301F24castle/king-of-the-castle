package com.example.king_of_the_castle_project;

import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.init;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class OrganizerActivityTest {

    @Before
    public void setUp() {
        // Initialize Intents
        Intents.init();
    }

    @Test
    public void testCreateEvent() {
        ActivityScenario.launch(OrganizerActivity.class);
        // click create event
        onView(withId(R.id.create_event_button)).perform(ViewActions.click());

        // Verify that the correct intent is launched
        intended(IntentMatchers.hasComponent(CreateEventActivity.class.getName()));
    }

    @Test
    public void testManageEvent() {
        ActivityScenario.launch(OrganizerActivity.class);
        // Click manage button
        onView(withId(R.id.manage_event_button)).perform(ViewActions.click());

        // Check if moved to right activity
        intended(IntentMatchers.hasComponent(ManageEventsActivity.class.getName()));
    }

    @Test
    public void testManageFacility() {
        ActivityScenario.launch(OrganizerActivity.class);
        // Simulate button click for managing facility
        onView(withId(R.id.manage_facility_button)).perform(ViewActions.click());

        // Correct intent
        intended(IntentMatchers.hasComponent(ManageFacilityActivity.class.getName()));
    }

    @Test
    public void testReturnButtonClick() {
        ActivityScenario.launch(OrganizerActivity.class).onActivity(activity -> {
            activity.findViewById(R.id.change_role_button).performClick();
            assertTrue(activity.isFinishing());
        });
    }

    @After
    public void tearDown() {
        // Release any resources and clean up after the tests
        Intents.release();
    }
}