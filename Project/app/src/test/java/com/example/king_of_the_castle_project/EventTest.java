package com.example.king_of_the_castle_project;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.Before;
//import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class EventTest {

    private Event event;

    // Test firebase constructor
    @Test
    public void testEmpty() {
        Event emptyEvent = new Event();
        assertNull(emptyEvent.getName());
        assertNull(emptyEvent.getDate());
        assertNull(emptyEvent.getTime());
        assertNull(emptyEvent.getLocation());
        assertNull(emptyEvent.getEventDetails());
        assertNull(emptyEvent.getWaitList());
        assertNull(emptyEvent.getQrCodeData());
        assertNull(emptyEvent.getAcceptedList());
        assertNull(emptyEvent.getDeclinedList());
        assertNull(emptyEvent.getRegisteredList());
        assertNull(emptyEvent.getGeolocation());
    }

    // Test constructor
    @Test
    public void testConstructor() {
        ArrayList<String> waitList = new ArrayList<>();
        waitList.add("user1");

        Event fullEvent = new Event("Event 1", "2024-12-01", "10:00 AM", "Location A", "Event details", 100, waitList, true);

        assertEquals("Event 1", fullEvent.getName());
        assertEquals("2024-12-01", fullEvent.getDate());
        assertEquals("10:00 AM", fullEvent.getTime());
        assertEquals("Location A", fullEvent.getLocation());
        assertEquals("Event details", fullEvent.getEventDetails());
        assertEquals(100, fullEvent.getMaxParticipants());
        assertEquals(waitList, fullEvent.getWaitList());
        assertTrue(fullEvent.getGeolocation());
    }

    // Test name getter, setter
    @Test
    public void testGetterSetterName() {
        event = new Event();
        event.setName("Test");
        assertEquals("Test", event.getName());
    }

    // Test date getter, setter
    @Test
    public void testGetterSetterDate() {
        event = new Event();
        event.setDate("2024-12-01");
        assertEquals("2024-12-01", event.getDate());
    }

    // Test time getter, setter
    @Test
    public void testGetterSetterTime() {
        event = new Event();
        event.setTime("10:00 AM");
        assertEquals("10:00 AM", event.getTime());
    }

    // Test location getter, setter
    @Test
    public void testGetterSetterLocation() {
        event = new Event();
        event.setLocation("Edmonton");
        assertEquals("Edmonton", event.getLocation());
    }

    // Test details getter, setter
    @Test
    public void testGetterSetterDetails() {
        event = new Event();
        event.setEventDetails("Event Details");
        assertEquals("Event Details", event.getEventDetails());
    }

    // Test participants getter, setter
    @Test
    public void testGetterSetterParticipants() {
        event = new Event();
        event.setMaxParticipants(50);
        assertEquals(50, event.getMaxParticipants());
    }

    // Test geolocation getter, setter
    @Test
    public void testGetterSetterGeolocation() {
        event = new Event();
        event.setGeolocation(true);
        assertTrue(event.getGeolocation());

        event.setGeolocation(false);
        assertFalse(event.getGeolocation());
    }

    // Test waitlist getter, setter
    @Test
    public void testGetterSetterWaitlist() {
        event = new Event();
        ArrayList<String> waitList = new ArrayList<>();
        waitList.add("test1");

        event.setWaitList(waitList);
        assertEquals(waitList, event.getWaitList());
    }
}