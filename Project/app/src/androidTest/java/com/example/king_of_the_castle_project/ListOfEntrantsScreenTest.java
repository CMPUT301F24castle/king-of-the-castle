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

    @BeforeAll
    public static void setUp() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CountDownLatch latch = new CountDownLatch(4);

        // Create the intent with waitlist
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ListOfEntrantsInEventScreen.class);
        intent.putExtra("Waitlist", mockWaitList());

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
        mockEvent.put("organizerID", mockOrganizerId);

        // Set up mock entrant 1 data
        Map<String, Object> entrant1Data = new HashMap<>();
        entrant1Data.put("name", "Entrant 1");
        entrant1Data.put("email", "Entrant1@gmail.com");
        entrant1Data.put("phone", "9852221111");
        entrant1Data.put("id", mockEntrant1());

        // Set up mock entrant 2 data
        Map<String, Object> entrant2Data = new HashMap<>();
        entrant2Data.put("name", "Entrant 2");
        entrant2Data.put("email", "Entrant2@gmail.com");
        entrant2Data.put("phone", "4859997777");
        entrant2Data.put("id", mockEntrant2());

        // Set up mock entrant 3 data
        Map<String, Object> entrant3Data = new HashMap<>();
        entrant3Data.put("name", "Entrant 3");
        entrant3Data.put("email", "Entrant3@gmail.com");
        entrant3Data.put("phone", null);
        entrant3Data.put("id", mockEntrant3());

        writeToFirestore("entrants", mockEntrant1(), entrant1Data, latch);
        writeToFirestore("entrants", mockEntrant2(), entrant2Data, latch);
        writeToFirestore("entrants", mockEntrant3(), entrant3Data, latch);
        writeToFirestore("events", "List Of Entrants Screen Test Event", mockEvent, latch);

    }

    private static String mockOrganizerId = "ojnvwrfert";

    private ArrayList<String> mockWaitListNoEntrants() {
        return new ArrayList<>(); // Return an empty waitlist
    }

    private static ArrayList<String> mockWaitList() {
        ArrayList<String> mockWaitList = new ArrayList<>();
        mockWaitList.add(mockEntrant1());
        mockWaitList.add(mockEntrant2());
        mockWaitList.add(mockEntrant3());
        return mockWaitList;
    }

    private static String mockEntrant1() {
        return "vno940tgb3w";
    }

    private static String mockEntrant2() {
        return "vwj09jgv2vwfr";
    }

    private static String mockEntrant3() {
        return "gio340v2u3jf";
    }


    @Test
    public void testIntentsReceivedWithNoEntrants() {
        // Create the intent with the empty waitlist
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ListOfEntrantsInEventScreen.class);
        intent.putExtra("Waitlist", mockWaitListNoEntrants());

        // Launch the activity with the intent
        try (ActivityScenario<ListOfEntrantsInEventScreen> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                // Verify the activity received the intent
                Intent receivedIntent = activity.getIntent();
                assertEquals(mockWaitListNoEntrants(), receivedIntent.getSerializableExtra("Waitlist"));
            });
        }
    }

    @Test
    public void testIntentsReceivedWithEntrants() {
        // Create the intent with waitlist
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ListOfEntrantsInEventScreen.class);
        intent.putExtra("Waitlist", mockWaitList());

        // Launch the activity with the intent
        try (ActivityScenario<ListOfEntrantsInEventScreen> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                // Verify the activity received the intent
                Intent receivedIntent = activity.getIntent();
                assertEquals(mockWaitList(), receivedIntent.getSerializableExtra("Waitlist"));
            });
        }
    }

    @Test
    public void testLOEScreenWithNoEntrantsOnWaitlist() throws InterruptedException {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CountDownLatch latch = new CountDownLatch(1);

        // Create the intent with the empty waitlist
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ListOfEntrantsInEventScreen.class);
        intent.putExtra("Waitlist", mockWaitListNoEntrants());

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
                .document("Event Details Screen Test Event")
                .set(mockEvent)
                .addOnSuccessListener(aVoid -> latch.countDown())
                .addOnFailureListener(e -> {
                    fail("Failed to set up mock event in Firestore: " + e.getMessage());
                    latch.countDown();
                });

        latch.await(); // Wait for the write operation to complete

        // Launch the activity with the intent
        try (ActivityScenario<ListOfEntrantsInEventScreen> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                // Access the ListView
                ListView listOfEntrantsLV = activity.findViewById(R.id.list_of_entrants);

                // Verify the adapter is set
                assertNotNull(listOfEntrantsLV.getAdapter());
                WaitListAdapter adapter = (WaitListAdapter) listOfEntrantsLV.getAdapter();

                // Check how many entrants are in the entrant list
                assertEquals(0, adapter.getCount(), "Adapter should contain 0 items.");

                TextView noResultsTV = activity.findViewById(R.id.no_results_textview);

                assertEquals(View.GONE, listOfEntrantsLV.getVisibility()); // Hidden
                assertEquals(View.VISIBLE, noResultsTV.getVisibility()); // Shown
            });
        }
    }

    private static void writeToFirestore(String collection, String document, Map<String, Object> data, CountDownLatch latch) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collection)
                .document(document)
                .set(data)
                .addOnSuccessListener(aVoid -> latch.countDown())
                .addOnFailureListener(e -> {
                    fail("Failed to write mock data to Firestore: " + e.getMessage());
                    latch.countDown();
                });
    }

    @Test
    public void testLOEScreenWithEntrantsOnWaitlist() throws InterruptedException {
        // Create the intent with waitlist
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ListOfEntrantsInEventScreen.class);
        intent.putExtra("Waitlist", mockWaitList());

        // Launch the activity with the intent
        try (ActivityScenario<ListOfEntrantsInEventScreen> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                // Access the ListView
                ListView listView = activity.findViewById(R.id.list_of_entrants);
                assertNotNull(listView);

                // Poll until the data is loaded or timeout occurs
                boolean dataLoaded = false; // Wait up to 5 seconds
                try {
                    dataLoaded = waitForData(listView, 5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                assertTrue(dataLoaded, "ListView data did not load within the timeout.");

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

    private boolean waitForData (ListView listView,int timeoutMillis) throws
    InterruptedException {
        int waited = 0;
        while (waited < timeoutMillis) {
            if (listView.getChildCount() > 0 && allItemsPopulated(listView)) {
                return true;
            }
            Thread.sleep(100); // Wait 100ms before re-checking
            waited += 100;
        }
        return false; // Timeout
    }

    private boolean allItemsPopulated (ListView listView){
        for (int i = 0; i < listView.getChildCount(); i++) {
            View itemView = listView.getChildAt(i);
            TextView nameTextView = itemView.findViewById(R.id.entrant_name_LOE_screen);
            if (nameTextView == null || nameTextView.getText().toString().equals("Loading...")) {
                return false;
            }
        }
        return true;
    }
}