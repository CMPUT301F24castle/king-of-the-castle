package com.example.king_of_the_castle_project;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

@RunWith(AndroidJUnit4.class)
public class ProfileActivityTest {

    @Test
    public void testShowProfileData() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        // Set up mock entrant 1 data
        Map<String, Object> entrant1Data = new HashMap<>();
        entrant1Data.put("name", "John Doe");
        entrant1Data.put("email", "john.doe@example.com");
        entrant1Data.put("phone", "1234567890");
        entrant1Data.put("id", "h283uhd23d");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("entrants")
                .document("h283uhd23d")
                .set(entrant1Data)
                .addOnSuccessListener(aVoid -> latch.countDown())
                .addOnFailureListener(e -> {
                    fail("Failed to write mock data to Firestore: " + e.getMessage());
                    latch.countDown();
                });

        latch.await();

        ProfileActivity.setAndroidID("h283uhd23d");

        try (ActivityScenario<ProfileActivity> scenario = ActivityScenario.launch(ProfileActivity.class)) {
            scenario.onActivity(activity -> {
                // Verify that profile data is displayed correctly

                // Check name
                TextView nameTextView = activity.findViewById(R.id.name_TV_profile_screen);
                assertEquals("Name: John Doe", nameTextView.getText().toString());

                // Check email
                TextView emailTextView = activity.findViewById(R.id.email_TV_profile_screen);
                assertEquals("Email: john.doe@example.com", emailTextView.getText().toString());

                // Check phone number
                TextView phoneTextView = activity.findViewById(R.id.phone_number_TV_profile_screen);
                assertEquals("Phone Number: 1234567890", phoneTextView.getText().toString());
            });
        }

        CountDownLatch latch1 = new CountDownLatch(1);

        // Delete the mock entrant from Firestore
        db.collection("entrants")
                .document("h283uhd23d")
                .delete()
                .addOnSuccessListener(aVoid -> latch1.countDown())
                .addOnFailureListener(e -> {
                    fail("Failed to delete mock entrant from Firestore: " + e.getMessage());
                    latch1.countDown();
                });

        latch1.await();
    }

    @Before
    public void setUp() {
        // Initialize intents
        Intents.init();
    }

    @Test
    public void testEditProfileClicked() {
        try (ActivityScenario<ProfileActivity> scenario = ActivityScenario.launch(ProfileActivity.class)) {
            onView(withId(R.id.edit_profile_button)).perform(click());

            // Verify that the NewActivity is started
            intended(hasComponent(EditProfileActivity.class.getName()));
        }
    }

    @After
    public void tearDown() {
        // Release intents
        Intents.release();
    }
}
