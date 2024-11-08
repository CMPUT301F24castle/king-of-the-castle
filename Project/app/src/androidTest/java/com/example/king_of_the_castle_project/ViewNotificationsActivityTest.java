package com.example.king_of_the_castle_project;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class ViewNotificationsActivityTest {

    @Before
    public void setUp() {
        Intents.init();  // initialize Intents
        ActivityScenario.launch(MyNotificationsActivity.class);  // launch activity
    }

    @Test
    public void testReturnButton() {
        // click spinner/dropdown
        onView(withId(R.id.return_button)).perform(click());

        // verify that EntrantScreenActivity started
        Intents.intended(IntentMatchers.hasComponent(EntrantScreenActivity.class.getName()));
    }

    @Test
    public void testOrganizerRoleNavigation() {
        // click spinner
        onView(withId(R.id.choose_role_spinner)).perform(click());
        // click organizer
        onView(withText("Organizer")).perform(click());

        // click confirm button
        onView(withId(R.id.confirm_role_button)).perform(click());

        // verify that OrganizerActivity started
        Intents.intended(IntentMatchers.hasComponent(OrganizerActivity.class.getName()));
    }

    @After
    public void tearDown() {
        Intents.release();  // release intents after tests
    }
}

