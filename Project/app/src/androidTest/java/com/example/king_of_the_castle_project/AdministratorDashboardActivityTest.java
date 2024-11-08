package com.example.king_of_the_castle_project;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static
        androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
@RunWith(AndroidJUnit4.class)

public class AdministratorDashboardActivityTest {
    @Test
    public void testChangeRole() {
        // start the activity
        ActivityScenario.launch(AdministratorDashboardActivity.class);
        Espresso.onView(withId(R.id.change_role_button)).perform(click());
        Espresso.onView(withId(R.id.choose_role_spinner)).check(matches(isDisplayed()));
    }

    @Test
    public void testBrowseEvents() {
        Espresso.onView(withId(R.id.browse_event_button)).perform(click());
        Espresso.onView(withId(R.id.event_list)).check(matches(isDisplayed()));
    }
}
