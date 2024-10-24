package com.example.king_of_the_castle_project;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//this class relies on the EventDetails class, which holds the waiting list and the number of entrants wanted

public class Lottery { ;
    private List<Entrant> selectedAttendees;

    public Lottery() {
        this.selectedAttendees = new ArrayList<>(); //selectedAttendees will be the list of entrants we randomly get from the waiting list
    }

    public void selectRandomEntrants(EventDetails eventDetails) { //EventDetails will hold the values of the list of entrants (waiting list) as well as the number of participants needed for the event
        int x = eventDetails.numberOfEntrants;  // Direct access to the private field
        List<Entrant> waitingList = eventDetails.waitingList;  // Direct access to the private field

        Random random = new Random();
        List<Entrant> tempWaitingList = new ArrayList<>(waitingList);

        while (selectedAttendees.size() < x && tempWaitingList.size() > 0) {
            int index = random.nextInt(tempWaitingList.size());
            Entrant selectedEntrant = tempWaitingList.remove(index);
            selectedAttendees.add(selectedEntrant);
        }
    }

    public List<Entrant> getSelectedAttendees() {
        return selectedAttendees; //returns the selected Attendees
    }



}
