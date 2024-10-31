package com.example.king_of_the_castle_project;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public class Event {
    private String name;
    private String date;
    private String time;
    private String location;
    private String eventDetails;
    private int maxParticipants;
    private WaitList waitList;
    private Boolean geolocation;


    public Event(String name, String date, String time, String location, String eventDetails, int maxParticipants, WaitList waitList, Boolean geolocation) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.location = location;
        this.eventDetails = eventDetails;
        this.maxParticipants = maxParticipants;
        this.waitList = waitList;
        this.geolocation = geolocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public WaitList getWaitList() {
        return waitList;
    }

    public void setWaitList(WaitList waitList) {
        this.waitList = waitList;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }
}
