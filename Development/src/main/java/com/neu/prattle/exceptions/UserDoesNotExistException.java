package com.neu.prattle.exceptions;

import com.neu.prattle.model.User;

/**
 * An representation of an error which is thrown where a request has been made
 * for finding a user object that does not exist in the database.
 * Refer {@link User#equals}
 * Refer {@link com.neu.prattle.service.UserService#findUser(String)}
 *
 * @author CS5500 Fall 2019 Teaching staff
 * @version dated 2019-10-06
 */
public class UserDoesNotExistException extends RuntimeException {
    private static final long serialVersionUID = -4845176561270017896L;

    /**
     * Class constructor.
     *
     * @param message custom message for the exception
     */
    public UserDoesNotExistException(String message) {
        super(message);
    }
}
