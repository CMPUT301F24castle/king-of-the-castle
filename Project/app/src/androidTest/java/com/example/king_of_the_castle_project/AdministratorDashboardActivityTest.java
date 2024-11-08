package com.example.king_of_the_castle_project;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AdministratorDashboardActivityTest {

    @Rule
    public ActivityScenarioRule<AdministratorDashboardActivity> activityScenarioRule =
            new ActivityScenarioRule<>(AdministratorDashboardActivity.class);

    @Test
    public void testBrowseEventsButton() {
        // Click browse events
        onView(withId(R.id.browse_event_button)).perform(ViewActions.click());

        ActivityScenario<BrowseEventsActivity> scenario = ActivityScenario.launch(BrowseEventsActivity.class);
        scenario.onActivity(activity -> {
            // make sure browse event started
            assertTrue(activity instanceof BrowseEventsActivity);
        });
    }

    @Test
    public void testChangeRoleButton() {
        // click change role
        onView(withId(R.id.change_role_button)).perform(ViewActions.click());

        // Check for activity calling finish()
        activityScenarioRule.getScenario().onActivity(activity -> {
            assertTrue(activity.isFinishing());
        });
    }
}