package com.example.king_of_the_castle_project;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CreateEventActivityTest {

    @Rule
    public ActivityScenarioRule<CreateEventActivity> activityRule = new ActivityScenarioRule<>(CreateEventActivity.class);

    @Test
    public void testGeolocationCheckbox() {
        // Test geolocation checkbox
        onView(withId(R.id.event_geolocation_checkbox)).perform(click()); // Click to check the checkbox
        onView(withId(R.id.event_geolocation_checkbox)).check(ViewAssertions.matches(ViewMatchers.isChecked()));
    }

    @Test
    public void testUploadImageButton() {
        // Test upload image
        onView(withId(R.id.upload_photo_button)).perform(click());
    }

    @Test
    public void testCreateEventTransition() {
        // Start activity
        try (ActivityScenario<CreateEventActivity> scenario = ActivityScenario.launch(CreateEventActivity.class)) {

            // Fill in the event details
            onView(withId(R.id.event_name_edit_text)).perform(typeText("Test Event"));
            onView(withId(R.id.event_date_edit_text)).perform(typeText("01/12/2024"));
            onView(withId(R.id.event_location_edit_text)).perform(typeText("Test Location"));
            onView(withId(R.id.event_details_edit_text)).perform(typeText("Test Details"));
            onView(withId(R.id.event_max_participants_edit_text)).perform(typeText("100"));

            // Try geolocation checkbox
            onView(withId(R.id.event_geolocation_checkbox)).perform(click());

            // Click the "Create Event" button
            onView(withId(R.id.create_event_button)).perform(click());
        }
    }

}