package com.example.king_of_the_castle_project;

import java.util.ArrayList;

public class Entrant extends User {
    private ArrayList<String> onWaitList;
    private ArrayList<String> onAcceptedList;
    private ArrayList<String> onDeclinedList;
    private ArrayList<String> onRegisteredList;

    /**
     * Empty constructor necessary to pass information to firebase
     */
    public void Entrant() {
        // Empty constructor for firebase
    }

    /**
     * Constructor of Entrant
     * @param name
     *  Name of entrant
     * @param email
     *  Email of entrant
     * @param phoneNumber
     *  Phone number of entrant
     * @param Id
     *  Device ID of entrant
     */
    public Entrant(String name, String email, String phoneNumber, String Id) {
        super(name, email, phoneNumber, Id);
        this.onWaitList = new ArrayList<String>();
        this.onAcceptedList = new ArrayList<String>();
        this.onDeclinedList = new ArrayList<String>();
        this.onRegisteredList = new ArrayList<String>();
    }

    /**
     * Gets onWaitList
     * @return
     *  List of events the for which entrant is on the wait list
     */
    public ArrayList<String> getOnWaitList() {
        return onWaitList;
    }

    /**
     * Gets onAcceptedList
     * @return
     *  List of events for which the entrant has won the lottery
     */
    public ArrayList<String> getOnAcceptedList() {
        return onAcceptedList;
    }

    /**
     * Gets onDeclinedList
     * @return
     *  List of events for which the user has declined/been cancelled
     */
    public ArrayList<String> getOnDeclinedList() {
        return onDeclinedList;
    }

    /**
     * Gets onRegisteredList
     * @return
     *  List of events for which the user has registered
     */
    public ArrayList<String> getOnRegisteredList() {
        return onRegisteredList;
    }


}
