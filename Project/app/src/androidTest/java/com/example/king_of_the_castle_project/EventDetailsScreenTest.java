package com.example.king_of_the_castle_project;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import android.content.Intent;
import android.widget.TextView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventDetailsScreenTest {
    private static String mockOrganizerId = "ojnvwrfert";

    @Rule
    public ActivityScenarioRule<EventDetailsScreen> activityScenarioRule =
            new ActivityScenarioRule<EventDetailsScreen>(
                    new Intent(ApplicationProvider.getApplicationContext(), EventDetailsScreen.class)
                            .putExtra("qr result", "Event Details Screen Test Event")
            );

    @Test
    public void testIntentsReceived() {
        // Verify the extras inside the Activity
        activityScenarioRule.getScenario().onActivity(activity -> {
            Intent intent = activity.getIntent();
            assertEquals("Event Details Screen Test Event", intent.getStringExtra("qr result"));
        });
    }

    @Test
    public void testEventShows() throws InterruptedException {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CountDownLatch latch = new CountDownLatch(1);

        // Set up mock event data
        Map<String, Object> mockEvent = new HashMap<>();
        mockEvent.put("name", "Event Details Screen Test Event");
        mockEvent.put("date", "01/12/2024");
        mockEvent.put("time", "10:00");
        mockEvent.put("location", "Test Location");
        mockEvent.put("details", "Test Details");
        mockEvent.put("maxParticipants", 5);
        mockEvent.put("waitlist", new ArrayList<String>());
        mockEvent.put("qrCodeData", "qrCodeData");
        mockEvent.put("organizerID", mockOrganizerId);

        // Write data to Firestore
        db.collection("events")
                .document("Event Details Screen Test Event")
                .set(mockEvent)
                .addOnSuccessListener(aVoid -> latch.countDown())
                .addOnFailureListener(e -> {
                    fail("Failed to set up mock event in Firestore: " + e.getMessage());
                    latch.countDown();
                });

        latch.await(); // Wait for the write operation to complete

        // Create a mock Intent with extras
        activityScenarioRule.getScenario().onActivity(activity -> {
            // Access the TextViews
            TextView eventNameTV = activity.findViewById(R.id.event_name_TV);
            TextView eventDateTV = activity.findViewById(R.id.date_TV);
            TextView eventTimeTV = activity.findViewById(R.id.time_TV);
            TextView eventLocationTV = activity.findViewById(R.id.venue_TV);
            TextView eventMaxParticipantsTV = activity.findViewById(R.id.max_participants_TV);
            TextView eventNotesTV = activity.findViewById(R.id.notes_TV);

            // Get the displayed text
            String eventName = eventNameTV.getText().toString();
            String eventDate = eventDateTV.getText().toString();
            String eventTime = eventTimeTV.getText().toString();
            String eventLocation = eventLocationTV.getText().toString();
            String eventMaxParticipants = eventMaxParticipantsTV.getText().toString();
            String eventNotes = eventNotesTV.getText().toString();

            // Assert the expected value
            assertEquals("Event Details Screen Test Event", eventName);
            assertEquals("  • Date: 01/12/2024", eventDate);
            assertEquals("  • Time: 10:00", eventTime);
            assertEquals("Max Participants: 5", eventMaxParticipants);
            assertEquals("  • Venue: Test Location", eventLocation);
            assertEquals("  • Test Details", eventNotes);
        });

        // Clean up: Delete the test event after all tests are complete
        CountDownLatch latch2 = new CountDownLatch(1);

        db.collection("events")
                .document("Event Details Screen Test Event")
                .delete()
                .addOnSuccessListener(aVoid -> latch2.countDown())
                .addOnFailureListener(e -> latch2.countDown());

        latch2.await(); // Wait for deletion to complete
    }
}