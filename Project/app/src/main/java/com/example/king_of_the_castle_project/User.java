package com.example.king_of_the_castle_project;

import java.io.Serializable;

/**
 * The abstract class from which all User types extend
 * Defines a variable for everything the users have in common, as well as getters and setters
 */
public abstract class User implements Serializable {
    /**
     * Defining variables for all users
     */
    private String name;
    private String email;
    private String phoneNumber;
    private String id;
    private String pfpData;

    /**
     * Empty constructor necessary to pass information to firebase
     */
    public User(){
        // Empty constructor for firebase
    }

    /**
     * Constructor of the User
     * @param name
     *  Name of user
     * @param email
     *  Email of user
     * @param phoneNumber
     *  Phone number of user stored as a strng
     * @param id
     *  Device ID of the user
     */
    public User(String name, String email, String phoneNumber, String id) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.id = id;
    }

    /**
     * Gets name of User
     * @return
     *  Name of the User
     */
    public String getName() {
        return name;
    }

    /**
     * Gets email of User
     * @return
     *  Email of the User
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets phone number of User
     * @return
     *  Phone number of the User
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Gets device ID of User
     * @return
     *  Device ID of the User
     */
    public String getId() {
        return id;
    }

    /**
     * Sets name of the User
     * @param name
     *  Name of the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets email of the User
     * @param email
     *  Email of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets phone number of the User
     * @param phoneNumber
     *  Phone number of the user
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Sets device ID of the User
     * @param id
     *  Device ID of the user
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the profile picture data for the user.
     * This method returns the data associated with the user's profile picture (PFP).
     *
     * @return A string containing the profile picture data, which may be a URL or base64 encoded image data.
     */
    public String getPfpData() {
        return pfpData;
    }

    /**
     * Sets the profile picture data for the user.
     * This method allows you to set the data associated with the user's profile picture (PFP).
     *
     * @param pfpData A string representing the profile picture data, which could be a URL or base64 encoded image data.
     */
    public void setPfpData(String pfpData) {
        this.pfpData = pfpData;
    }
}
