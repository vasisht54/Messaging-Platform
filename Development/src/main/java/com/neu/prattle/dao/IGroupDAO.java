package com.neu.prattle.dao;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.neu.prattle.model.Group;

/**
 * this interface represents the operations implemented on a group.
 */
public interface IGroupDAO {

    /**
     * Add a new group to the pool of existing group.
     *
     * @param group to be added
     */
    void addGroup(Group group);

    /**
     * Find a group within all of the groups.
     *
     * @param name of the group to select.
     * @return the group
     */
    Group findGroup(String name);

    /**
     * Remove a group from the pool of existing groups.
     *
     * @param name to be removed
     */
    DeleteResult removeGroup(String name);

    /**
     * Update a group's information and fields in the database.
     *
     * @param group to update
     */
    UpdateResult updateGroup(Group group);

}
