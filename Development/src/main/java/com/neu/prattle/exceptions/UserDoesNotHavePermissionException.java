package com.neu.prattle.exceptions;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;

import java.io.Serializable;

/**
 * An representation of an error which is thrown where a request has been made
 * for removing a moderator, and the caller is not themselves a moderator.
 * In short, only moderators can remove other moderators.
 * Refer {@link User#equals}
 * Refer {@link Group#getListOfModerators()}
 * Refer {@link com.neu.prattle.service.UserService#findUser(String)}
 * Refer {@link com.neu.prattle.service.GroupService#removeModerator(Group, User, User)}
 */
public class UserDoesNotHavePermissionException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -5778294526794962601L;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public UserDoesNotHavePermissionException(String message) {
        super(message);
    }
}
