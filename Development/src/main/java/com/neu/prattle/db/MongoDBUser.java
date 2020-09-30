package com.neu.prattle.db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;

/**
 * this class provides the connection to the mongo server.
 */
public class MongoDBUser implements Database {

    private static final String err = "Operation not supported.";
    private static DBConnection conn = new DBConnection();
    private static String userName = "username";
    private MongoCollection<User> users;

    /**
     * Class constructor; instantiates the database connection with a predefined connection String
     */
    public MongoDBUser() {
        Properties properties = new Properties();
        try {
            properties.load(Objects
                    .requireNonNull(getClass().getClassLoader().getResourceAsStream("mongodb.properties")));
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        String dbCollectionName = properties.getProperty("db.collection.name");
        String dbCollectionUsers = properties.getProperty("db.collection.users");
        this.users = conn.getConnection().getDatabase(dbCollectionName)
                .getCollection(dbCollectionUsers, User.class);
    }

    /**
     * Add a new user to the database using its username as the first entry.
     *
     * @param user object to be added
     */
    public void addUser(User user) {
        this.users.insertOne(user);
    }

    /**
     * Add a new group to the database.
     *
     * @param group object to be added
     */
    public void addGroup(Group group) {
        throw new UnsupportedOperationException(err);
    }

    /**
     * Remove a user from the database using its username as the identifier.
     *
     * @param username of the user to be removed
     */
    public void removeUser(String username) {
        this.users.deleteOne(eq(userName, username));
    }

    /**
     * Remove a group from the database.
     *
     * @param name of the group to be removed
     * @return
     */
    public DeleteResult removeGroup(String name) {
        throw new UnsupportedOperationException(err);
    }

    /**
     * Find a user in the database using its username as the identifier.
     *
     * @param username of the user to find
     * @return the user
     */
    public User findUser(String username) {
        // find user in db
        return users.find(eq(userName, username)).projection(excludeId()).first();
    }

    /**
     * Find a group in the database.
     *
     * @param name of the user to find
     * @return the user
     */
    public Group findGroup(String name) {
        throw new UnsupportedOperationException(err);
    }


    /**
     * Update a user's information and fields in the database.
     *
     * @param user to update
     */
    public void updateUser(User user) {
        this.users.replaceOne(eq(userName, user.getUsername()), user);
    }

    /**
     * Update a group's information and fields in the database.
     *
     * @param group to update
     * @return
     */
    public UpdateResult updateGroup(Group group) {
        throw new UnsupportedOperationException(err);
    }

    /**
     * Find all users in the database.
     *
     * @return a list of all users
     */
    public MongoIterable<User> findUsers() {
        // find user in db
        return users.find();
    }
}
