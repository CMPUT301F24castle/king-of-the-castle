package com.example.king_of_the_castle_project;

import java.util.ArrayList;

public class Event {
    private String name;
    private String date;
    private String time;
    private String location;
    private String eventDetails;
    private int maxParticipants;
    private ArrayList<String> waitList;
    private ArrayList<String> acceptedList;
    private ArrayList<String> declinedList;
    private ArrayList<String> registeredList;
    private Boolean geolocation;
    private String qrCodeData;

    /**
     * Empty event constructor necessary for passing data to firebase
     */
    public Event() {
        // Empty constructor for firebase
    }

    public Event(String name, String date, String time, String location, String eventDetails, int maxParticipants, Boolean geolocation) {
        // Constructor with no waitlist because it was causing bugs
        this.name = name;
        this.date = date;
        this.time = time;
        this.location = location;
        this.eventDetails = eventDetails;
        this.maxParticipants = maxParticipants;
        this.geolocation = geolocation;
        this.waitList = new ArrayList<String>();
        this.acceptedList = new ArrayList<String>();
        this.declinedList = new ArrayList<String>();
        this.registeredList = new ArrayList<String>();
    }

    /**
     * Event class storing metadata about events that users want to enter or create
     * @param name
     *      The name of the event
     * @param date
     *      The date that the event is going to occur
     * @param time
     *      The time for which the event will happen
     * @param location
     *      Where the event is taking place
     * @param eventDetails
     *      Any extra notes from the organizer
     * @param maxParticipants
     *      Optional capacity limit on number of people who can participate in the event
     * @param waitList
     *      A list of entrants who want to enter the event
     * @param geolocation
     *      Boolean to determine if the organizer would like to check entrant location for attending event
     */
    public Event(String name, String date, String time, String location, String eventDetails, int maxParticipants, ArrayList<String> waitList, Boolean geolocation) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.location = location;
        this.eventDetails = eventDetails;
        this.maxParticipants = maxParticipants;
        this.waitList = waitList;
        this.geolocation = geolocation;
        this.acceptedList = new ArrayList<String>();
        this.declinedList = new ArrayList<String>();
        this.registeredList = new ArrayList<String>();
    }

    /**
     * Constructor with everything
     * @param name
     *  The name of the event
     * @param date
     *  The date that the event is going to occur
     * @param time
     *  The time for which the event will happen
     * @param location
     *  Where the event is taking place
     * @param eventDetails
     *  Any extra notes from the organizer
     * @param maxParticipants
     *  Optional capacity limit on number of people who can participate in the event
     * @param waitList
     *  A list of entrants who want to enter the event
     * @param geolocation
     *  Boolean to determine if the organizer would like to check entrant location for attending event
     * @param acceptedList
     *  List of entrant ids accepted to the event
     * @param declinedList
     *  List of entrant ids declined from the event
     * @param registeredList
     *  List of entrant ids registered for the event
     */
    public Event(String name, String date, String time, String location, String eventDetails, int maxParticipants, ArrayList<String> waitList, ArrayList<String> acceptedList, ArrayList<String> declinedList, ArrayList<String> registeredList, Boolean geolocation) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.location = location;
        this.eventDetails = eventDetails;
        this.maxParticipants = maxParticipants;
        this.waitList = waitList;
        this.acceptedList = acceptedList;
        this.declinedList = declinedList;
        this.registeredList = registeredList;
        this.geolocation = geolocation;
    }

    /**
     * Getter for name attribute
     * @return
     *      Returns event name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name attribute
     * @param name
     *      Desired event name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for event date
     * @return
     *      Returns the event date
     */
    public String getDate() {
        return date;
    }

    /**
     * Setter for date
     * @param date
     *      Takes in the event date to set it to
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Getter for time
     * @return
     *      Returns the time variable
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the time variable
     * @param time
     *      Takes in time to set it to
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Getter for the location variable
     * @return
     *      Returns the value of the location variable
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location variable
     * @param location
     *      Takes in a location to set the location to
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Getter for geolocation boolean
     * @return
     *      Returns true or false depending whether organizer wants geolocation
     */
    public Boolean getGeolocation() {
        return geolocation;
    }

    /**
     * Setter for the geolocation variable
     * @param geolocation
     *      Takes in a boolean to either set geolocation to true or false
     */
    public void setGeolocation(Boolean geolocation) {
        this.geolocation = geolocation;
    }

    /**
     * Getter for the entrant waitlist
     *
     * @return Returns a list of entrants
     */
    public ArrayList<String> getWaitList() {
        return waitList;
    }

    /**
     * Setter for the waitlist class
     * @param waitList
     *      Sets the data for the waitlist
     */
    public void setWaitList(ArrayList<String> waitList) {
        this.waitList = waitList;
    }

    /**
     * Get organizer notes or event details
     * @return
     *      Return the contents of event details variable
     */
    public String getEventDetails() {
        return eventDetails;
    }

    /**
     * set the data in eventdetails variable
     * @param eventDetails
     *      Sets the eventdetails variable
     */
    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    /**
     * Getter for maxparticipants attribute
     * @return
     *      Returns the value of the maxParticipants variable
     */
    public int getMaxParticipants() {
        return maxParticipants;
    }

    /**
     * Setter for maxParticipants variable
     * @param maxParticipants
     *      Takes in an int to set the maxparticipants to
     */
    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    /**
     * Getter class for qrcodedata
     * @return
     *      Returns the qrcodedata as a 64byte string
     */
    public String getQrCodeData() {
        return qrCodeData;
    }

    /**
     * Sets the qrCodeData variable
     * @param qrCodeData
     *      Takes in a 64byte string for us to hold the QR data
     */
    public void setQrCodeData(String qrCodeData) {
        this.qrCodeData = qrCodeData;
    }

    public ArrayList<String> getAcceptedList() {
        return acceptedList;
    }

    public ArrayList<String> getDeclinedList() {
        return declinedList;
    }

    public ArrayList<String> getRegisteredList() {
        return registeredList;
    }

    public void setAcceptedList(ArrayList<String> acceptedList) {
        this.acceptedList = acceptedList;
    }

    public void setDeclinedList(ArrayList<String> declinedList) {
        this.declinedList = declinedList;
    }

    public void setRegisteredList(ArrayList<String> registeredList) {
        this.registeredList = registeredList;
    }
}