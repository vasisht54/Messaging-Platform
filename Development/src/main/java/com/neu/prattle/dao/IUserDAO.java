package com.neu.prattle.dao;

import com.mongodb.client.MongoIterable;
import com.neu.prattle.exceptions.UserDoesNotExistException;
import com.neu.prattle.model.User;

/**
 * this interface represents the operations on a user.
 */
public interface IUserDAO {

    /**
     * Add a new user to the pool of existing user.
     *
     * @param user to be added
     */
    void addUser(User user);

    /**
     * Delete user from the pool of existing users; a user can be deleted by using its unique
     * username.
     *
     * @param username of the user to be deleted
     */
    void removeUser(String username);

    /**
     * Find a user from the database using its username as the identifier.
     *
     * @param username of the user to find
     * @return true is user exists, otherwise return false
     * @throws UserDoesNotExistException if the user does not exist
     */
    User findUser(String username);

    /**
     * Find all users in the database.
     *
     * @return a list of all users
     */
    MongoIterable<User> findUsers();

    /**
     * Update a user's information and fields in the database.
     *
     * @param user to update
     */
    void updateUser(User user);
}
