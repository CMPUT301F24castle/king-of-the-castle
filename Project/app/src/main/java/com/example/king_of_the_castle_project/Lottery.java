package com.example.king_of_the_castle_project;
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
    public void selectRandomEntrants(Event eventDetails) {
        int x = eventDetails.getMaxParticipants();
        WaitList waitingList = eventDetails.getWaitList();

        Random random = new Random();

        while (selectedAttendees.size() < x && waitingList.size() > 0) {
            int index = random.nextInt(waitingList.size());
            Entrant selectedEntrant = waitingList.remove(index);
            selectedAttendees.add(selectedEntrant);
        }
    }

    /**
     * Returns the selectedAttendees list
     * @return
     */
    public List<Entrant> getSelectedAttendees() {
        return selectedAttendees; //returns the selected Attendees
    }




}