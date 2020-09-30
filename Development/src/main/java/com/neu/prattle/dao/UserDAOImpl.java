package com.neu.prattle.dao;

import com.mongodb.client.MongoIterable;
import com.neu.prattle.db.Database;
import com.neu.prattle.db.MongoDBUser;
import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.exceptions.UserDoesNotExistException;
import com.neu.prattle.model.User;

/**
 * This class represents a concrete implementation of the DatabaseDAO interface.
 */
public class UserDAOImpl implements IUserDAO {

    private static Database db = new MongoDBUser();

    /**
     * Add a new user to the pool of existing user.
     *
     * @param user to be added
     */
    public void addUser(User user) {
        // check if username is taken
        usernameIsAvailable(user.getUsername());
        db.addUser(user);
    }

    /**
     * Private method to check is user already exists.
     *
     * @param username to find
     * @throws UserAlreadyPresentException if user already exists
     */
    private void usernameIsAvailable(String username) {
        User user = db.findUser(username);

        try {
            // if user doc is empty, user does not exist
            if (user.getUsername().equals(username)) {
                String str = String.format("%s is taken. Please, choose a different username", username);
                throw new UserAlreadyPresentException(str);
            }
        } catch (NullPointerException e) {
            // do nothing, since user does not exist
        }
    }

    /**
     * Delete user from the pool of existing users; a user can be deleted by using its unique
     * username.
     *
     * @param username of the user to be deleted
     */
    public void removeUser(String username) {
        // Check first to see if user exists before removing. Returns user if exists.
        if (findUser(username) != null) {
            db.removeUser(username);
        }
    }

    /**
     * Find a user from the database using its username as the indentifier.
     *
     * @param username of the user to find
     * @return true is user exists, otherwise return false
     * @throws UserDoesNotExistException if the user does not exist
     */
    public User findUser(String username) {
        // retrieve user document
        User user = db.findUser(username);

        if (user == null) {
            String str = String.format("%s does not exist.", username);
            throw new UserDoesNotExistException(str);
        }

        return user;
    }

    /**
     * Find all users in the database.
     *
     * @return a list of all users
     */
    public MongoIterable<User> findUsers() {
        // retrieve all users

        return db.findUsers();
    }

    /**
     * Update a user's information and fields in the database.
     *
     * @param user to update
     */
    public void updateUser(User user) {
        if (findUser(user.getUsername()) != null) {
            db.updateUser(user);
        }
    }
}
