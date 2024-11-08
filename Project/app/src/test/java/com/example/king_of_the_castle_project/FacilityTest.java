package com.example.king_of_the_castle_project;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.Before;
//import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class FacilityTest {

    private Facility facility;

    // Test the getters
    @Test
    public void testGetters() {
        facility = new Facility("Test", "Edmonton", "7801234567");
        assertEquals("Test", facility.getName());
        assertEquals("Edmonton", facility.getLocation());
        assertEquals("7801234567", facility.getPhoneNumber());
    }

    // test the getters and setters for name
    @Test
    public void testName() {
        facility = new Facility("Test", "Edmonton", "7801234567");
        facility.setName("abcdefg");
        assertEquals("abcdefg", facility.getName());
    }

    // test location
    @Test
    public void testLocation() {
        facility = new Facility("Test", "Edmonton", "7801234567");
        facility.setLocation("somewhere");
        assertEquals("somewhere", facility.getLocation());
    }

    // Test phone number
    @Test
    public void testSetPhoneNumber() {
        facility = new Facility("Test", "Edmonton", "7801234567");
        facility.setPhoneNumber("12");
        assertEquals("12", facility.getPhoneNumber());
    }
}