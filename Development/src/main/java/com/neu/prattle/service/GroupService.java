package com.neu.prattle.service;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Acts as an interface between the data layer and the servlet controller for groups.
 * <p>
 * The controller is responsible for interfacing with this instance of groups to perform all CRUD
 * operators on groups.
 */
public interface GroupService {

    /**
     * Find a group within all of the groups.
     *
     * @param name of the group to select.
     * @return the group
     */
    Group findGroup(String name);

    /**
     * Adds a group to the master list of groups in the app.
     *
     * @param group A group object when created with parameters passed in from the Creator.
     */
    void addGroup(Group group);

    /**
     * Adds a user to a group.
     *
     * @param user User object.
     */
    Response addMembers(String groupName, String memberName, User user);

    /**
     * Add a moderator to an existing group.
     *
     * @param groupName     name of the group to add the moderator to
     * @param moderatorName name of the new moderator
     * @param user          logged-in user object
     */
    Response addModerator(String groupName, String moderatorName, User user);

    /**
     * Removes a group from the list of all groups.
     *
     * @param groupName name of the group to be removed.
     * @return delete result
     */
    Response removeGroup(String groupName, User user);

    /**
     * Removes an individual user from a group.
     *
     * @param user       logged-in user object
     * @param groupName  name of the group from which the user is removed
     * @param memberName name of the member to be removed
     */
    Response removeMember(String groupName, String memberName, User user);

    /**
     * Removes a moderator from a group.
     *
     * @param groupName     name of Group to remove a moderator from
     * @param moderatorName name of the specific moderator to be removed
     * @param user          logged-in user object
     */
    Response removeModerator(String groupName, String moderatorName, User user);

    /**
     * Getter method to return the members of a group.
     *
     * @param groupName name of group to call getter on
     * @return response status with entity as list of members
     */
    Response getGroupMembers(String groupName);

    /**
     * Getter method to return the moderators of a group.
     *
     * @param groupName name of group to call getter on.
     * @return response status with entity as list of moderators.
     */
    Response getGroupModerators(String groupName);

    /**
     * Method that merges groupB into groupA
     *
     * @param parentGroup resulting group's name
     * @param childGroup  group that's getting merged
     * @return response status after merging
     */
    Response mergeGroups(String parentGroup, String childGroup, User moderator);

    /**
     * retrieves all the groups in database.
     *
     * @return list of all groups for that user
     */
    List<String> findAllGroups(String username);
}
