package com.example.king_of_the_castle_project;

public class Facility {
    private String name;
    private String location;
    private String phoneNumber;

    /**
     * Constructor for Facility assigning attributes name, location, phoneNumber
     * @param name
     *      Name associated with facility
     * @param location
     *      Location associated with facility
     * @param phoneNumber
     *      Phone number associated with facility
     */
    public Facility(String name, String location, String phoneNumber) {
        this.name = name;
        this.location = location;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Getter for the facility name
     * @return
     *      Returns facility name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for facility name
     * @param name
     *      Takes in a string name to set the facility name to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for location variable
     * @return
     *      Returns string location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Setter for location variable
     * @param location
     *      Takes in a string representing the location of the event
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Getter for facility phoneNumber variable
     * @return
     *      returns the phoneNumber variable value
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Setter for the phone number class
     * @param phoneNumber
     *      Takes in a string representing the organizers phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
