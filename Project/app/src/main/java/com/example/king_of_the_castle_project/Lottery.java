package com.example.king_of_the_castle_project;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



/**
 * This is a class that defines the Lottery method to randomly choose x amount of entrants and add them to a list called selectedAttendees
 */
public class Lottery { ;
    private List<String> selectedAttendees;

    public Lottery(){
        this.selectedAttendees = new ArrayList<>(); //selectedAttendees will be the list of entrants we randomly get from the waiting list
    }

    /**
     * This selects x amount of random entrants from the waitingList (from EventDetails class)
     * @param eventDetails
     * this is the event object to use
     */
    public void selectRandomEntrants(Event eventDetails, int numberOfEntrants) {
        //int x = eventDetails.getMaxParticipants();
        ArrayList<String> waitingList = eventDetails.getWaitList();  // Direct access to the private field

        Random random = new Random();
        //ArrayList<Entrant> tempWaitingList = new ArrayList<Entrant>();

        while (selectedAttendees.size() < numberOfEntrants && waitingList.size() > 0) {
            int index = random.nextInt(waitingList.size());
            String selectedEntrant = waitingList.remove(index);
            selectedAttendees.add(selectedEntrant);
            //eventDetails.getAcceptedList().add(selectedEntrant);
        }

    }

    /**
     * Returns the selectedAttendees list
     * @return
     */
    public List<String> getSelectedAttendees() {
        return selectedAttendees; //returns the selected Attendees
    }

}