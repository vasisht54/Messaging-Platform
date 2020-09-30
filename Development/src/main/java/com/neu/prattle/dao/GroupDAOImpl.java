package com.neu.prattle.dao;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.neu.prattle.db.Database;
import com.neu.prattle.db.MongoDBGroup;
import com.neu.prattle.model.Group;

/**
 * This class represents a concrete implementation of the DatabaseDAO interface.
 */
public class GroupDAOImpl implements IGroupDAO {

    private static Database db = new MongoDBGroup();

    /**
     * Add a new group to the pool of existing group.
     *
     * @param group to be added
     */
    @Override
    public void addGroup(Group group) {
        db.addGroup(group);
    }

    /**
     * Remove a group from the pool of existing groups.
     *
     * @param name to be removed
     * @return delete result
     */
    @Override
    public DeleteResult removeGroup(String name) {
        return db.removeGroup(name);
    }

    /**
     * Find a group within the database.
     *
     * @param name of the group to find
     * @return a group object
     */
    @Override
    public Group findGroup(String name) {
        return db.findGroup(name);
    }

    /**
     * Update a group's information and fields in the database.
     *
     * @param group to update
     */
    @Override
    public UpdateResult updateGroup(Group group) {
        return db.updateGroup(group);
    }

}
