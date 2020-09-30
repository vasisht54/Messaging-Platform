package com.neu.prattle.service;

import com.neu.prattle.model.User;

import java.util.List;


/**
 * Acts as an interface between the data layer and the
 * servlet controller.
 * <p>
 * The controller is responsible for interfacing with this instance
 * to perform all the CRUD operations on user accounts.
 *
 * @author CS5500 Fall 2019 Teaching staff
 * @version dated 2019-10-06
 */
public interface UserService {
    /**
     * Returns an optional object which might be empty or wraps an object
     * if the System contains a {@link User} object having the same name
     * as the parameter.
     *
     * @param username The name of the user
     * @return Optional object.
     */
    User findUser(String username);

    /**
     * Find all users in the database.
     *
     * @return a list of all users
     */
    List<User> findUsers();

    /**
     * Add user to the database.
     *
     * @param user to be added
     */
    void addUser(User user);

    /**
     * Remove user from the database.
     *
     * @param username to be added
     */
    void removeUser(String username);

    /**
     * Update user fields from the database.
     *
     * @param user to modify.
     */
    void updateUser(User user);
}
