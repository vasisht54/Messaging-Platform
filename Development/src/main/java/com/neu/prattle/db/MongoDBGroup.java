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
public class MongoDBGroup implements Database {

    private static final String err = "Operation not supported.";
    private static DBConnection conn = new DBConnection();
    private static String groupName = "groupName";
    private MongoCollection<Group> groups;

    /**
     * Class constructor; instantiates the database connection with a predefined connection String
     */
    public MongoDBGroup() {
        Properties properties = new Properties();
        try {
            properties.load(Objects
                    .requireNonNull(getClass().getClassLoader().getResourceAsStream("mongodb.properties")));
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        String dbCollectionName = properties.getProperty("db.collection.name");
        String dbCollectionGroup = properties.getProperty("db.collection.groups");
        this.groups = conn.getConnection().getDatabase(dbCollectionName)
                .getCollection(dbCollectionGroup, Group.class);
    }

    /**
     * Add a new user to the database using its username as the first entry.
     *
     * @param user object to be added
     */
    public void addUser(User user) {
        throw new UnsupportedOperationException(err);
    }

    /**
     * Add a new group to the database.
     *
     * @param group object to be added
     */
    public void addGroup(Group group) {
        groups.insertOne(group);
    }

    /**
     * Remove a user from the database using its username as the indentifier.
     *
     * @param username of the user to be removed
     */
    public void removeUser(String username) {
        throw new UnsupportedOperationException(err);
    }

    /**
     * Remove a group from the database.
     *
     * @param name of the group to be removed
     * @return
     */
    public DeleteResult removeGroup(String name) {
        return groups.deleteOne(eq(groupName, name));
    }

    /**
     * Find a user in the database using its username as the indentifier.
     *
     * @param username of the user to find
     * @return the user
     */
    public User findUser(String username) {
        throw new UnsupportedOperationException(err);
    }

    /**
     * Find all users in the database.
     *
     * @return a list of all users
     */
    public MongoIterable<User> findUsers() {
        throw new UnsupportedOperationException(err);
    }

    /**
     * Find a group in the database.
     *
     * @param name of the user to find
     * @return the user
     */
    public Group findGroup(String name) {
        // find user in db
        return groups.find(eq(groupName, name)).projection(excludeId()).first();
    }


    /**
     * Update a user's information and fields in the database.
     *
     * @param user to update
     */
    public void updateUser(User user) {
        throw new UnsupportedOperationException(err);
    }

    /**
     * Update a groups's information and fields in the database.
     *
     * @param group to update
     * @return the update result
     */
    public UpdateResult updateGroup(Group group) {
        return groups.replaceOne(eq(groupName, group.getGroupName()), group);
    }
}
