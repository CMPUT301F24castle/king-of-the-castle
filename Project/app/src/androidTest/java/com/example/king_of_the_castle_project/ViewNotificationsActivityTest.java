package com.example.king_of_the_castle_project;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.app.Activity;

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
        ActivityScenario.launch(EntrantScreenActivity.class);  // launch activity
    }

    /**
     * Testing invite button
     */
    @Test
    public void testInviteButton() {
        onView(withId(R.id.invitations_button)).perform(click());

        // verify that Loss screen started
        Intents.intended(IntentMatchers.hasComponent(MyNotificationsActivity.class.getName()));
    }

    /**
     * Testing more button
     */
    @Test
    public void testMoreButton() {
        onView(withId(R.id.invitations_button)).perform(click());
        onView(withId(R.id.more_button)).perform(click());

        // verify that Loss screen started
        Intents.intended(IntentMatchers.hasComponent(MyLossNotificationsActivity.class.getName()));
    }

    /**
     * Tests the return button on the previous page
     */
    @Test
    public void testReturnButton() {
        onView(withId(R.id.invitations_button)).perform(click());

        onView((withId(R.id.return_button))).perform(click());
        Intents.intended(IntentMatchers.hasComponent(EntrantScreenActivity.class.getName()));

    }

    /**
     * Testing second return button
     */
    @Test
    public void testSecondReturnButton() {
        onView(withId(R.id.invitations_button)).perform(click());
        onView(withId(R.id.more_button)).perform(click());

        // verify that Loss screen started
        Intents.intended(IntentMatchers.hasComponent(MyLossNotificationsActivity.class.getName()));

        onView((withId(R.id.return_button))).perform(click());
        Intents.intended(IntentMatchers.hasComponent(MyNotificationsActivity.class.getName()));

    }

    @After
    public void tearDown() {
        Intents.release();  // release intents after tests
    }
}

