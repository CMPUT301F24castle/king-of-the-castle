package com.example.king_of_the_castle_project;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import android.content.Intent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class ListOfEntrantsScreenTest {

    private static void writeToFirestoreWaitlist(String collection, String eventID, String entrantID, Map<String, String> data, CountDownLatch latch) {
        if (eventID == null || entrantID == null) {
            throw new IllegalArgumentException("Event ID and entrant ID must not be null.");
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collection)
                .document(eventID)
                .collection("waitlist")
                .document(entrantID)
                .set(data)
                .addOnSuccessListener(aVoid -> latch.countDown())
                .addOnFailureListener(e -> {
                    fail("Failed to write mock data to Firestore: " + e.getMessage());
                    latch.countDown();
                });
    }

    private static void writeToFirestore(String collection, String document, Map<String, Object> data, CountDownLatch latch) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection(collection)
//                .document(document)
//                .set(data)
//                .addOnSuccessListener(aVoid -> latch.countDown())
//                .addOnFailureListener(e -> {
//                    fail("Failed to write mock data to Firestore: " + e.getMessage());
//                    latch.countDown();
//                });
    }

    private static String mockOrganizerId = "ojnvwrfert";

    private ArrayList<String> mockWaitListNoEntrants() {
        return new ArrayList<>(); // Return an empty waitlist
    }

    private static ArrayList<String> mockWaitList() {
        ArrayList<String> mockWaitList = new ArrayList<>();
        mockWaitList.add(mockEntrant1().get("entrantID"));
        mockWaitList.add(mockEntrant2().get("entrantID"));
        mockWaitList.add(mockEntrant3().get("entrantID"));
        return mockWaitList;
    }

    private static Map<String, String> mockEntrant1() {
        Map<String, String> entrant1WaitlistData = new HashMap<>();
        entrant1WaitlistData.put("entrantID", "evkl30h4ign45g");
        entrant1WaitlistData.put("latitude", "0.0");
        entrant1WaitlistData.put("longitude", "0.0");
        return entrant1WaitlistData;
    }

    private static Map<String, String> mockEntrant2() {
        Map<String, String> entrant2WaitlistData = new HashMap<>();
        entrant2WaitlistData.put("entrantID", "ql93ojevwef");
        entrant2WaitlistData.put("latitude", "0.0");
        entrant2WaitlistData.put("longitude", "0.0");
        return entrant2WaitlistData;
    }

    private static Map<String, String> mockEntrant3() {
        Map<String, String> entrant3WaitlistData = new HashMap<>();
        entrant3WaitlistData.put("entrantID", "nvp2490p3wek4f");
        entrant3WaitlistData.put("latitude", "0.0");
        entrant3WaitlistData.put("longitude", "0.0");
        return entrant3WaitlistData;
    }


    @Test
    public void testIntentsReceivedWithNoEntrants() {
        // Create the intent with the empty waitlist
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ListOfFilteredEntrantsInEventScreen.class);
        intent.putExtra("entrant_id_list", mockWaitListNoEntrants());

        // Launch the activity with the intent
        try (ActivityScenario<ListOfFilteredEntrantsInEventScreen> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                // Verify the activity received the intent
                Intent receivedIntent = activity.getIntent();
                assertEquals(mockWaitListNoEntrants(), receivedIntent.getSerializableExtra("entrant_id_list"));
            });
        }
    }

    @Test
    public void testIntentsReceivedWithEntrants() {
        // Create the intent with waitlist
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ListOfFilteredEntrantsInEventScreen.class);
        intent.putExtra("entrant_id_list", mockWaitList());

        // Launch the activity with the intent
        try (ActivityScenario<ListOfFilteredEntrantsInEventScreen> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                // Verify the activity received the intent
                Intent receivedIntent = activity.getIntent();
                assertEquals(mockWaitList(), receivedIntent.getSerializableExtra("entrant_id_list"));
            });
        }
    }

    @Test
    public void testLOEScreenWithNoEntrantsOnWaitlist() throws InterruptedException {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CountDownLatch latch = new CountDownLatch(1);

        // Create the intent with the empty waitlist
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ListOfFilteredEntrantsInEventScreen.class);
        intent.putExtra("entrant_id_list", mockWaitListNoEntrants());

        // Set up mock event data
        Map<String, Object> mockEvent = new HashMap<>();
        mockEvent.put("name", "List Of Entrants Screen Test Event");
        mockEvent.put("date", "01/15/2024");
        mockEvent.put("time", "11:00");
        mockEvent.put("location", "Test Location");
        mockEvent.put("details", "Test Details");
        mockEvent.put("maxParticipants", 10);
        mockEvent.put("waitlist", mockWaitListNoEntrants());
        mockEvent.put("qrCodeData", "qrCodeData");
        mockEvent.put("organizerID", mockOrganizerId);

        // Write event data to Firestore
        db.collection("events")
                .document("List Of Entrants Screen Test Event")
                .set(mockEvent)
                .addOnSuccessListener(aVoid -> latch.countDown())
                .addOnFailureListener(e -> {
                    fail("Failed to set up mock event in Firestore: " + e.getMessage());
                    latch.countDown();
                });

        latch.await(); // Wait for the write operation to complete

        // Launch the activity with the intent
        try (ActivityScenario<ListOfFilteredEntrantsInEventScreen> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                // Access the ListView
                ListView listOfEntrantsLV = activity.findViewById(R.id.list_of_entrants);

                TextView noResultsTV = activity.findViewById(R.id.no_results_textview);

                assertEquals(View.GONE, listOfEntrantsLV.getVisibility()); // Hidden
                assertEquals(View.VISIBLE, noResultsTV.getVisibility()); // Shown
            });
        }
/*
        // Delete the test event document
        db.collection("events")
                .document("testEventId")
                .delete()
                .addOnSuccessListener(aVoid -> latch.countDown())
                .addOnFailureListener(e -> {
                    System.err.println("Failed to delete Test Event: " + e.getMessage());
                    latch.countDown();
                });*/
    }

    @Test
    public void testLOEScreenWithEntrantsOnWaitlist() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(7);

        // Set up mock event data
        Map<String, Object> mockEvent = new HashMap<>();
        mockEvent.put("name", "List Of Entrants Screen Test Event");
        mockEvent.put("date", "01/15/2024");
        mockEvent.put("time", "11:00");
        mockEvent.put("location", "Test Location");
        mockEvent.put("details", "Test Details");
        mockEvent.put("maxParticipants", 10);
        mockEvent.put("waitlist", mockWaitList());
        mockEvent.put("qrCodeData", "qrCodeData");
        mockEvent.put("geolocation", false);
        mockEvent.put("organizerID", mockOrganizerId);

        // Set up mock entrant 1 data
        Map<String, Object> entrant1Data = new HashMap<>();
        entrant1Data.put("name", "Entrant 1");
        entrant1Data.put("email", "Entrant1@gmail.com");
        entrant1Data.put("phone", "9852221111");
        entrant1Data.put("id", mockEntrant1().get("entrantID"));

        // Set up mock entrant 2 data
        Map<String, Object> entrant2Data = new HashMap<>();
        entrant2Data.put("name", "Entrant 2");
        entrant2Data.put("email", "Entrant2@gmail.com");
        entrant2Data.put("phone", "4859997777");
        entrant2Data.put("id", mockEntrant2().get("entrantID"));

        // Set up mock entrant 3 data
        Map<String, Object> entrant3Data = new HashMap<>();
        entrant3Data.put("name", "Entrant 3");
        entrant3Data.put("email", "Entrant3@gmail.com");
        entrant3Data.put("phone", null);
        entrant3Data.put("id", mockEntrant3().get("entrantID"));

        writeToFirestore("entrants", mockEntrant1().get("entrantID"), entrant1Data, latch);
        writeToFirestore("entrants", mockEntrant2().get("entrantID"), entrant2Data, latch);
        writeToFirestore("entrants", mockEntrant3().get("entrantID"), entrant3Data, latch);
        writeToFirestore("events", "List Of Entrants Screen Test Event", mockEvent, latch);


        writeToFirestoreWaitlist("events", "List Of Entrants Screen Test Event", mockEntrant1().get("entrantID"), mockEntrant1(), latch);
        writeToFirestoreWaitlist("events", "List Of Entrants Screen Test Event", mockEntrant2().get("entrantID"), mockEntrant2(), latch);
        writeToFirestoreWaitlist("events", "List Of Entrants Screen Test Event", mockEntrant3().get("entrantID"), mockEntrant3(), latch);

        // Create the intent with waitlist
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ListOfFilteredEntrantsInEventScreen.class);
        intent.putExtra("entrant_id_list", mockWaitList());

        // Launch the activity with the intent
        try (ActivityScenario<ListOfFilteredEntrantsInEventScreen> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                // Access the ListView
                ListView listView = activity.findViewById(R.id.list_of_entrants);

                // Assert data in each ListView item
                for (int i = 0; i < listView.getChildCount(); i++) {
                    View itemView = listView.getChildAt(i);
                    TextView nameTextView = itemView.findViewById(R.id.entrant_name_LOE_screen);
                    TextView emailTextView = itemView.findViewById(R.id.entrant_email_LOE_screen);
                    TextView phoneTextView = itemView.findViewById(R.id.entrant_phone_number_LOE_screen);

                    if (i == 0) {
                        assertEquals("Entrant 1", nameTextView.getText().toString());
                        assertEquals("Entrant1@gmail.com", emailTextView.getText().toString());
                        assertEquals("9852221111", phoneTextView.getText().toString());
                    } else if (i == 1) {
                        assertEquals("Entrant 2", nameTextView.getText().toString());
                        assertEquals("Entrant2@gmail.com", emailTextView.getText().toString());
                        assertEquals("4859997777", phoneTextView.getText().toString());
                    } else if (i == 2) {
                        assertEquals("Entrant 3", nameTextView.getText().toString());
                        assertEquals("Entrant3@gmail.com", emailTextView.getText().toString());
                        assertEquals("No phone number provided", phoneTextView.getText().toString());
                    }
                }
            });
        }
        }
}