package com.neu.prattle.db;

import com.mongodb.client.MongoIterable;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;

/**
 * this interface represents the methods for Database operations.
 */
public interface Database {

    /**
     * Add a new user to the database using its username as the first entry.
     *
     * @param user object to be added
     */
    void addUser(User user);

    /**
     * Add a new group to the database.
     *
     * @param group object to be added
     */
    void addGroup(Group group);

    /**
     * Remove a user from the database using its username as the indentifier.
     *
     * @param username of the user to be removed
     */
    void removeUser(String username);

    /**
     * Remove a group from the database.
     *
     * @param name of the group to be removed
     */
    DeleteResult removeGroup(String name);

    /**
     * Find a user from the database using its username as the indentifier.
     *
     * @param username of the user to find
     * @return true is user exists, otherwise return false
     */
    User findUser(String username);

    /**
     * Find all users in the database.
     *
     * @return a list of all the users
     */
    MongoIterable<User> findUsers();

    /**
     * Find a group in the database.
     *
     * @param name of the user to find
     * @return the group
     */
    Group findGroup(String name);

    /**
     * Update a user's information and fields in the database.
     *
     * @param user to update
     */
    void updateUser(User user);

    /**
     * Update a groups's information and fields in the database.
     *
     * @param group to update
     */
    UpdateResult updateGroup(Group group);

}
