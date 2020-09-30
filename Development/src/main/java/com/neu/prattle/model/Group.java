package com.neu.prattle.model;

import com.neu.prattle.exceptions.UserDoesNotExistException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Group of users. Members within a group can send messages to all other
 * members, each Group can have or multiple moderators, Groups can be merge.
 */
public class Group {

    private String groupUsername;
    private String groupName;
    private String description;
    private List<User> listOfUsers;
    private List<User> listOfModerators;
    private String password;

    public Group() {
        this.listOfUsers = new ArrayList<>();
        this.listOfModerators = new ArrayList<>();
    }

    /**
     * Overloaded class constructor with parameters.
     *
     * @param groupName   - Name of group.
     * @param description - Description of group.
     */
    public Group(String groupName, String description, User groupCreator) {
        this.groupName = groupName;
        this.description = description;
        this.listOfUsers = new ArrayList<>();
        this.listOfModerators = new ArrayList<>();
        this.listOfUsers.add(groupCreator);
        this.listOfModerators.add(groupCreator);
        this.password = null;
    }

    public Group(String groupName, String description, User groupCreator, String password) {
        this(groupName, description, groupCreator);
        this.password = password;
    }

    /**
     * Setter method for the group's name. Acts as the UUID of the group. This is enforced by a Set
     * data structure containing groups, and also by the DB methods?
     */
    public String getGroupUsername() {
        return groupUsername;
    }

    /**
     * Getter method for the group's name. Acts as the UUID of the group. This is enforced by a Set
     * data structure containing groups, and also by the DB methods?
     *
     * @param groupUsername of the group
     */
    public void setGroupUsername(String groupUsername) {
        this.groupUsername = groupUsername;
    }

    /**
     * Getter method for the group's name. Acts as the UUID of the group. This is enforced by a Set
     * data structure containing groups, and also by the DB methods?
     *
     * @return String of the group's name.
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Setter method for the group's name. Acts as the UUID of the group. This is enforced by a Set
     * data structure containing groups, and also by the DB methods?
     *
     * @param groupName String for the group's name.
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Getter method to return the group's description / topic.
     *
     * @return String of the description text.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter method to set the group's description / topic.
     *
     * @param description String of the description text.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter method to return the members of a group.
     *
     * @return Unique members of a group.
     */
    public List<User> getListOfUsers() {
        return this.listOfUsers;
    }

    public void setListOfUsers(List<User> listOfUsers) {
        this.listOfUsers = listOfUsers;
    }

    /**
     * Getter method to return the moderators, which are a subset members of a group.
     *
     * @return Unique moderators of a group.
     */
    public List<User> getListOfModerators() {
        return this.listOfModerators;
    }

    public void setListOfModerators(List<User> listOfModerators) {
        this.listOfModerators = listOfModerators;
    }

    /**
     * Adds a user to the list of members for a group. Checks to see if there is password, if so then
     * verify the password entered before performing operation.
     *
     * @param user User to add.
     */
    public void addUsers(User user) {
        listOfUsers.add(user);
    }

    /**
     * Overlaod addUsers() method so multiple users can be added at a time.
     *
     * @param users List of users to added.
     */
    public void addUsers(List<User> users) {
        listOfUsers.addAll(users);
    }

    /**
     * Add all members of another group to the current group.
     *
     * @param other Group where we get the list of users to add
     */
    public void addGroup(Group other) {
        listOfUsers.addAll(other.getListOfUsers());
    }

    /**
     * Removes a member from the group.
     *
     * @param user User to remove from the group.
     */
    public void removeUser(User user) {
        if (!listOfUsers.contains(user)) {
            String str = String.format("%s is not in this group", user.getUsername());
            throw new UserDoesNotExistException(str);
        }
        listOfModerators.remove(user);
        listOfUsers.remove(user);
    }

    /**
     * Removes a moderator from the moderator list, they still remain in the group however. To fully
     * remove a moderator, you should call removeUser instead.
     *
     * @param user moderator to remove
     */
    public void addModerator(User user) {
        listOfUsers.add(user);
        listOfModerators.add(user);
    }

    /**
     * Removes a moderator from the moderator list, they still remain in the group however. To fully
     * remove a moderator, you should call removeUser instead.
     *
     * @param user moderator to remove
     */
    public void removeModerator(User user) {
        listOfModerators.remove(user);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
