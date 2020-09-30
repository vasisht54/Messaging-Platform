package com.neu.prattle.service;

import com.neu.prattle.dao.IUserDAO;
import com.neu.prattle.dao.UserDAOImpl;
import com.neu.prattle.model.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of {@link UserService}
 * <p>
 * It stores the user accounts in-memory, which means any user accounts
 * created will be deleted once the application has been restarted.
 *
 * @author CS5500 Fall 2019 Teaching staff
 * @version dated 2019-10-06
 */
public class UserServiceImpl implements UserService {
    private static UserService accountService;

    static {
        accountService = new UserServiceImpl();
    }

    private IUserDAO db = new UserDAOImpl();


    /**
     * UserServiceImpl is a Singleton class.
     */
    UserServiceImpl() {
    }

    /**
     * Call this method to return an instance of this service.
     *
     * @return this
     */
    public static UserService getInstance() {
        return accountService;
    }

    /**
     * Find user within set of users.
     *
     * @param username of the user
     * @return an optional wrapper supplying the user
     */
    public User findUser(String username) {
        return this.db.findUser(username);
    }

    /**
     * Find all users in the database.
     *
     * @return a list of all users
     */
    public List<User> findUsers() {
        List<User> users = new ArrayList<>();

        Iterator iterator = this.db.findUsers().iterator();

        while (iterator.hasNext()) {
            users.add((User) iterator.next());
        }

        return users;
    }

    /**
     * Add user to the database.
     *
     * @param user to be added
     */
    public synchronized void addUser(User user) {
        if (user == null) {
            throw new NullPointerException("New user cannot be null.");
        }
        this.db.addUser(user);
    }

    /**
     * Remove user from the database.
     *
     * @param username to be added
     */
    public synchronized void removeUser(String username) {
        this.db.removeUser(username);
    }

    /**
     * Update user fields from the database.
     *
     * @param user to modify.
     */
    @Override
    public void updateUser(User user) {
        this.db.updateUser(user);
    }
}
