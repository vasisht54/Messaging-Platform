package com.neu.prattle.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A User object represents a basic account information for a user.
 *
 * @author CS5500 Fall 2019 Teaching staff
 * @version dated 2019-10-06
 */
public class User {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String primaryLanguage;
    private List<String> listOfGroups = new ArrayList<>();

    /**
     * Class constructor.
     */
    public User() {
    }

    /**
     * Overload class constructor with parameters.
     *
     * @param username        of the user
     * @param password        of the user
     * @param firstName       of the user
     * @param lastName        of the user
     * @param primaryLanguage of the user
     */
    public User(String username, String password, String firstName, String lastName, String primaryLanguage) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.primaryLanguage = primaryLanguage;
    }

    /**
     * Getter method to return the user's username.
     *
     * @return the user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter method for user's username.
     *
     * @param username of the sender
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter method to return the user's password.
     *
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter method for user's password.
     *
     * @param password of the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter method to return the user's first name.
     *
     * @return the user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter method for user's first name.
     *
     * @param firstName of the user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter method to return the user's last name.
     *
     * @return the user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter method for user's last name.
     *
     * @param lastName of the user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Getter method to return the user's primary language.
     *
     * @return the user's primary language
     */
    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    /**
     * Setter method to set a user's primary language; this field is used
     * to determine in which language a user will receive message.
     *
     * @param language preference of the user
     */
    public void setPrimaryLanguage(String language) {
        this.primaryLanguage = language;
    }

    public List<String> getListOfGroups() {
        return listOfGroups;
    }

    public void setListOfGroups(List<String> listOfGroups) {
        this.listOfGroups = listOfGroups;
    }

    public void addToGroupList(String groupName) {
        this.listOfGroups.add(groupName);
    }

    public void removeFromGroupList(String groupName) {
        this.listOfGroups.remove(groupName);
    }

    /**
     * Returns the hashCode of this object.
     * <p>
     * As username can be treated as a sort of identifier for
     * this instance, we can use the hashCode of "username"
     * for the complete object.
     *
     * @return hashCode of "this"
     */
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    /**
     * Makes comparison between two user accounts. Two user objects are equal
     * if their username are equal (usernames are case-sensitive).
     *
     * @param obj Object to compare
     * @return a predicate value for the comparison.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User))
            return false;

        User user = (User) obj;
        return user.username.equals(this.username);
    }
}
