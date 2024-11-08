package com.example.king_of_the_castle_project;

import static org.junit.Assert.assertEquals;

//import org.junit.Test;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

/**
 * Tests getters and setters for entrant class
 */
public class EntrantTest {
    private Entrant entrant = new Entrant("name", "email", "phone", "id");

    /**
     * Tests getting name
     */
    @Test
    public void getNameTest() {
        assertEquals(entrant.getName(), "name");
    }

    /**
     * Tests getting email
     */
    @Test
    public void getEmailTest() {
        assertEquals(entrant.getEmail(), "email");
    }

    /**
     * Tests getting phone number
     */
    @Test
    public void getPhoneTest() {
        assertEquals(entrant.getPhoneNumber(), "phone");
    }

    /**
     * Tests getting id
     */
    @Test
    public void getIdTest() {
        assertEquals(entrant.getId(), "id");
    }

    /**
     * Tests setting name
     */
    @Test
    public void setNameTest() {
        entrant.setName("name2");
        assertEquals(entrant.getName(), "name2");
    }

    /**
     * Tests setting email
     */
    @Test
    public void setEmailTest() {
        entrant.setEmail("email2");
        assertEquals(entrant.getEmail(), "email2");
    }

    /**
     * Tests setting phone number
     */
    @Test
    public void setPhoneTest() {
        entrant.setPhoneNumber("phone2");
        assertEquals(entrant.getPhoneNumber(), "phone2");
    }

    /**
     * Tests setting id
     */
    @Test
    public void setIdTest() {
        entrant.setId("id2");
        assertEquals(entrant.getId(), "id2");
    }

}
