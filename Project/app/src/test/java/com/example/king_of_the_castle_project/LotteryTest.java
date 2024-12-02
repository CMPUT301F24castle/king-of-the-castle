package com.example.king_of_the_castle_project;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.Before;
//import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class LotteryTest {


    private ArrayList<String> testwaitlist;
    private Lottery testlottery;
    private Event testevent;


    @Test
    public void testAddEntrantsToWaitList() {
        testwaitlist = new ArrayList<String>();
        // Create some entrants
        Entrant entrant1 = new Entrant("Alice", "alice@example.com", "555-1234", "id1");
        Entrant entrant2 = new Entrant("Bob", "bob@example.com", "555-5678", "id2");

        // Add entrants to the waitlist
        testwaitlist.add(entrant1.getId());
        testwaitlist.add(entrant2.getId());

        // Assert the waitlist contains the correct number of entrants
        assertEquals(2, testwaitlist.size());
        assertEquals("id1", testwaitlist.get(0));
        assertEquals("id2", testwaitlist.get(1));
    }

    @Test
    public void testSelectCorrectNumberOfEntrants() {
        testwaitlist = new ArrayList<String>();
        // Create some entrants
        Entrant entrant1 = new Entrant("Alice", "alice@example.com", "555-1234", "id1");
        Entrant entrant2 = new Entrant("Bob", "bob@example.com", "555-5678", "id2");
        Entrant entrant3 = new Entrant("carl", "carl@example.com", "555-5788", "id3");

        // Add entrants to the waitlist
        testwaitlist.add(entrant1.getId());
        testwaitlist.add(entrant2.getId());
        testwaitlist.add(entrant3.getId());
        testlottery = new Lottery();

        // Call the lottery selection method
        testevent = new Event("Castle Tour", "2024-11-15", "10:00 AM", "Castle Grounds",
                "A guided tour of the castle", 2, true, "testID");
        testlottery.selectRandomEntrants(testevent);

        // Verify that the correct number of entrants were selected (2 in this case)
        List<String> selectedAttendees = testlottery.getSelectedAttendees();
        assertEquals(testevent.getMaxParticipants(), selectedAttendees.size(),
                "Lottery should select the correct number of entrants.");
        //System.out.println("Selected Entrants:");

        //for (String entrant : selectedAttendees) {
        //    System.out.println("Name: " + entrant.getName() + ", Email: " + entrant.getEmail());
        //}

    }


}
