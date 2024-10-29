package com.example.king_of_the_castle_project;

import java.sql.Time;
import java.util.Date;

public class Event {
    private Date date;
    private Time time;
    private Facility facility;
    private String notes;
    private int maxParticipants;
    private WaitList waitList;

    public Event(Date date, Time time, Facility facility, String notes, int maxParticipants) {
        this.date = date;
        this.time = time;
        this.facility = facility;
        this.notes = notes;
        this.maxParticipants = maxParticipants;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Facility getFacility() {
        return facility;
    }

    public String getNotes() {
        return notes;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public WaitList getWaitList() {
        return waitList;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public void setWaitList(WaitList waitList) {
        this.waitList = waitList;
    }
}
