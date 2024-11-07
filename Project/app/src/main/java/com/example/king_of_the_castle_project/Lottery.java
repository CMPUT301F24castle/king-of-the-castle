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
        List<String> waitingList = eventDetails.getWaitList();

        Random random = new Random();

        while (selectedAttendees.size() < x && !waitingList.isEmpty()) {
            int index = random.nextInt(waitingList.size());
            String selectedEntrantId = waitingList.remove(index);
            selectedAttendees.add(selectedEntrantId);
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