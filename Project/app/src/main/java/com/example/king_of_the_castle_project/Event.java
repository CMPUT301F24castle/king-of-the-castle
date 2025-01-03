package com.example.king_of_the_castle_project;

import com.google.protobuf.Any;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

/**
 * Event class holding metadata for any events created or accessed by users
 */
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
    private String organizerID;
    private String hashIdentifier;
    private Map<String, String> facility;
    private Boolean qrCodeValid;

    /**
     * Empty event constructor necessary for passing data to firebase
     */
    public Event() {
        // Empty constructor for firebase
    }

    /**
     * Constructor without a waitList
     * @param name
     *  Name of event
     * @param date
     *  Date of event
     * @param time
     *  Time of event
     * @param location
     *  Location of event
     * @param eventDetails
     *  Notes added by the organizer
     * @param maxParticipants
     *  Maximum number of participants
     * @param geolocation
     *  Whether geolocation is enabled or not
     */
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
        this.organizerID = "";
    }

    /**
     *Constructor without a waitList
     * @param name
     *  Name of event
     * @param date
     *  Date of event
     * @param time
     *  Time of event
     * @param location
     *  Location of event
     * @param eventDetails
     *  Notes added by the organizer
     * @param maxParticipants
     *  Maximum number of participants
     * @param geolocation
     *  Whether geolocation is enabled or not
     * @param organizerID
     *  Android ID of the organizer
     */
    public Event(String name, String date, String time, String location, String eventDetails, int maxParticipants, Boolean geolocation, String organizerID) {
        // Constructor with no waitlist because it was causing bugs
        this.name = name;
        this.date = date;
        this.time = time;
        this.location = location;
        this.eventDetails = eventDetails;
        this.maxParticipants = maxParticipants;
        this.geolocation = geolocation;
        this.organizerID = organizerID;
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
    public Event(String name, String date, String time, String location, String eventDetails, int maxParticipants, ArrayList<String> waitList, Boolean geolocation, String organizerID) {
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
        this.organizerID = organizerID;
    }

    /**
     * Constructor for when a waitlist is being retrieved from firebase
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
     * @param acceptedList
     *  List of entrant ids accepted to the event
     * @param declinedList
     *  List of entrant ids declined from the event
     * @param registeredList
     *  List of entrant ids registered for the event
     * @param geolocation
     *  Boolean to determine if the organizer would like to check entrant location for attending event
     * @param qrCodeData
     *  A string used to represent the qr code
     * @param organizerID
     *  A string used to hold the android ID of the organizer
     */
    public Event(String name, String date, String time, String location, String eventDetails, int maxParticipants, ArrayList<String> waitList, ArrayList<String> acceptedList, ArrayList<String> declinedList, ArrayList<String> registeredList, Boolean geolocation, String qrCodeData, String organizerID, String hashIdentifier) {
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
        this.organizerID = organizerID;
        this.qrCodeData = qrCodeData;
        this.hashIdentifier = hashIdentifier;
    }

    /**
     * Getter for organizerID attribute
     * @return
     *      Returns organizer ID
     */
    public String getOrganizerID() {
        return organizerID;
    }

    /**
     * Setter for organizerID attribute
     * @param organizerID
     *      Desired organizerID
     */
    public void setOrganizerID(String organizerID) {
        this.organizerID = organizerID;
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

    /**
     * Gets list of accepted entrants
     * @return
     *  List of accepted entrant ids
     */
    public ArrayList<String> getAcceptedList() {
        return acceptedList;
    }

    /**
     * Gets list of declined/cancelled entrants
     * @return
     *  List of declined/cancelled entrant ids
     */
    public ArrayList<String> getDeclinedList() {
        return declinedList;
    }

    /**
     * Gets list of registered entrants
     * @return
     *  List of registered entrant ids
     */
    public ArrayList<String> getRegisteredList() {
        return registeredList;
    }

    /**
     * Sets list of accepted entrants
     * @param acceptedList
     *  List of accepted entrant ids
     */
    public void setAcceptedList(ArrayList<String> acceptedList) {
        this.acceptedList = acceptedList;
    }

    /**
     * Sets list of declined/cancelled entrants
     * @param declinedList
     *  List of declined/cancelled entrant ids
     */
    public void setDeclinedList(ArrayList<String> declinedList) {
        this.declinedList = declinedList;
    }

    /**
     * Sets list of registered entrants
     * @param registeredList
     *  List of registered entrant ids
     */
    public void setRegisteredList(ArrayList<String> registeredList) {
        this.registeredList = registeredList;
    }

    /**
     * Returns the identifier for the event
     * @return
     *      Hashed identifier for the event
     */
    public String getHashIdentifier() {
        return hashIdentifier;
    }

    /**
     * Sets the unique hash identifier of the current event
     * @param hashIdentifier
     *      An identifier created by a hash function
     */
    public void setHashIdentifier(String hashIdentifier) {
        this.hashIdentifier = hashIdentifier;
    }

    /**
     * Gets facility of event
     * @return
     *  Facility of the event
     */
    public Map<String, String> getFacility() {
        return facility;
    }

    /**
     * Sets facility of event
     * @param facility
     *  Proposed facility of the event
     */
    public void setFacility(Map<String, String> facility) {
        this.facility = facility;
    }

    /**
     * Tells whether QR code is valid
     * @return
     *  Boolean value of whether QR code is valid
     */
    public Boolean getQrCodeValid() {
        return qrCodeValid;
    }

    /**
     * Sets if QR code is valid
     * @param qrCodeValid
     *  Boolean of whether event QR code is valid
     */
    public void setQrCodeValid(Boolean qrCodeValid) {
        this.qrCodeValid = qrCodeValid;
    }
}