package com.example.king_of_the_castle_project;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BrowseProfilesActivityTest {

    @Test
    public void testReturnButton() {
        // launch activity
        ActivityScenario<BrowseProfilesActivity> scenario = ActivityScenario.launch(BrowseProfilesActivity.class);

        // perform click on return button
        onView(withId(R.id.return_button_LOE_screen)).perform(click());

        // make sure activity is done
        scenario.moveToState(Lifecycle.State.DESTROYED);
    }
}
