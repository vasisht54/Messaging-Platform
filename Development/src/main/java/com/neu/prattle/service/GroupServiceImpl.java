package com.neu.prattle.service;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.neu.prattle.dao.GroupDAOImpl;
import com.neu.prattle.dao.IGroupDAO;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Implementation of {@link GroupService}
 * <p>
 * It stores the groups in-memory, which means any groups created will be deleted once the
 * application has been restarted.
 */
public class GroupServiceImpl implements GroupService {

    private static GroupService groupService;
    private static UserService userService;
    private static String notFound = "User or Group does not exist";
    private static String denied = "Permission Denied";

    static {
        groupService = new GroupServiceImpl();
        userService = new UserServiceImpl();
    }

    private IGroupDAO groupDao = new GroupDAOImpl();

    /**
     * A Singleton class.
     */
    private GroupServiceImpl() {
    }

    /**
     * Call this method to return an instance of this service.
     *
     * @return this
     */
    public static GroupService getInstance() {
        return groupService;
    }

    /**
     * Find a group within all of the groups.
     *
     * @param name of the group to find
     * @return the group
     */
    @Override
    public Group findGroup(String name) {
        return groupDao.findGroup(name);
    }

    /**
     * Adds a group to the master list of groups in the app.
     *
     * @param group A group object when created with parameters passed in from the Creator.
     */
    @Override
    public void addGroup(Group group) {
        try {
            User user = group.getListOfUsers().get(0);
            user.addToGroupList(group.getGroupName());
            userService.updateUser(user);
            groupDao.addGroup(group);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Operation failed");
        }
    }

    /**
     * Removes a group from the list of all groups.
     *
     * @param groupName name of the group to be removed.
     * @return response of delete result
     */
    @Override
    public Response removeGroup(String groupName, User user) {
        Group group = findGroup(groupName);
        if (group != null) {
            if (isModerator(group, user)) {
                DeleteResult result = groupDao.removeGroup(groupName);
                if (result.getDeletedCount() == 1) {
                    return Response.ok().build();
                } else {
                    return Response.status(409).build();
                }
            } else {
                return Response.status(409).entity(denied).build();
            }
        } else {
            return Response.status(409).entity("Group does not exist").build();
        }
    }

    /**
     * Adds a user to a group. Checks to see if there is password, if so then verify the password
     * entered before performing operation.
     *
     * @param groupName name of the group to add to
     * @param user      object to be added
     */
    @Override
    public Response addMembers(String groupName, String memberName, User user) {
        Group group = findGroup(groupName);
        User newMember = userService.findUser(memberName);
        if (group == null || newMember == null) {
            return Response.status(409).entity("User or Group does not exist").build();
        }
        if (isModerator(group, user)) {
            if (!isUserPresent(group, newMember) && !isModerator(group, newMember)) {
                newMember.addToGroupList(group.getGroupName());
                userService.updateUser(newMember);
                group.addUsers(newMember);
                return updateGroup(group);
            } else {
                return Response.status(409).entity("User already present").build();
            }
        } else {
            return Response.status(409).entity(denied).build();
        }
    }

    /**
     * Add a moderator to an existing group.
     *
     * @param groupName name of the group to add the moderator to
     * @param user      to add as a moderator
     */
    @Override
    public Response addModerator(String groupName, String moderatorName, User user) {
        Group group = findGroup(groupName);
        User newModerator = userService.findUser(moderatorName);
        if (group == null || newModerator == null) {
            return Response.status(409).entity(notFound).build();
        }
        if (isModerator(group, user)) {
            if (!isUserPresent(group, newModerator) && !isModerator(group, newModerator)) {
                newModerator.addToGroupList(group.getGroupName());
                userService.updateUser(newModerator);
                group.addModerator(newModerator);
                group.addUsers(newModerator);
                return updateGroup(group);
            } else if (isUserPresent(group, newModerator) && !isModerator(group, newModerator)) {
                group.addModerator(newModerator);
                return updateGroup(group);
            } else {
                return Response.status(409).entity("User is already a moderator").build();
            }
        } else {
            return Response.status(409).entity(denied).build();
        }
    }

    /**
     * Removes an individual user from a group.
     *
     * @param groupName  name of group to remove from
     * @param memberName User object of user to be removed
     * @param user       User object of logged in user
     */
    @Override
    public Response removeMember(String groupName, String memberName, User user) {
        Group group = findGroup(groupName);
        User member = userService.findUser(memberName);
        if (group == null || member == null) {
            return Response.status(409).entity(notFound).build();
        }
        if (isModerator(group, user)) {
            if (isUserPresent(group, member)) {
                member.removeFromGroupList(group.getGroupName());
                userService.updateUser(member);
                group.removeUser(member);
                return updateGroup(group);
            } else {
                return Response.status(409).entity("User not found").build();
            }
        } else {
            return Response.status(409).entity(denied).build();
        }
    }

    /**
     * Removes a moderator from a group. Confirms that the caller is a moderator before removing
     * another moderator.
     *
     * @param groupName     name of group to remove from
     * @param moderatorName name of new moderator to be added
     * @param user          User object of logged in user
     */
    @Override
    public Response removeModerator(String groupName, String moderatorName, User user) {
        Group group = findGroup(groupName);
        User newModerator = userService.findUser(moderatorName);
        if (group == null || newModerator == null) {
            return Response.status(409).entity(notFound).build();
        }
        if (isModerator(group, user)) {
            if (isModerator(group, newModerator)) {
                group.removeModerator(newModerator);
                return updateGroup(group);
            } else {
                return Response.status(409).entity("Moderator not found").build();
            }
        } else {
            return Response.status(409).entity(denied).build();
        }
    }

    /**
     * Private method to check if a user is already a moderator.
     *
     * @param group to which the user is added
     * @param user  user to add
     */
    private boolean isModerator(Group group, User user) {
        return group.getListOfModerators().contains(user);
    }


    /**
     * Private method to check if a user is already present in the users list.
     *
     * @param group  to which the user is added
     * @param member user to add
     */
    private boolean isUserPresent(Group group, User member) {
        return group.getListOfUsers().contains(member);
    }

    /**
     * Getter method to return the members of a group.
     *
     * @param groupName Group to call getter on.
     * @return A list of members.
     */
    @Override
    public Response getGroupMembers(String groupName) {
        Group group = findGroup(groupName);
        if (group != null) {
            return Response.ok().entity(group.getListOfUsers()).build();
        } else {
            return Response.status(409).entity("Group not found").build();
        }
    }

    /**
     * Getter method to return the moderators of a group.
     *
     * @param groupName Group to call getter on.
     * @return A list of moderators.
     */
    @Override
    public Response getGroupModerators(String groupName) {
        Group group = findGroup(groupName);
        if (group != null) {
            return Response.ok().entity(group.getListOfModerators()).build();
        } else {
            return Response.status(409).entity("Group not found").build();
        }
    }

    @Override
    public List<String> findAllGroups(String username) {
        User user = userService.findUser(username);
        return user.getListOfGroups();
    }

    private Response updateGroup(Group group) {
        UpdateResult result = groupDao.updateGroup(group);
        if (result.wasAcknowledged()) {
            return Response.ok().build();
        } else {
            return Response.status(409).build();
        }
    }

    @Override
    public Response mergeGroups(String groupAName, String groupBName, User moderator) {
        Group groupA = findGroup(groupAName);
        Group groupB = findGroup(groupBName);
        if (groupA == null || groupB == null) {
            return Response.status(409).entity(notFound).build();
        }
        if (isModerator(groupA, moderator)) {
            for (User member : groupB.getListOfUsers()) {
                if (!groupA.getListOfUsers().contains(member)) {
                    groupA.addUsers(member);
                }
            }
            return updateGroup(groupA);
        } else {
            return Response.status(409).entity(denied).build();
        }
    }
}
