package com.neu.prattle.controller;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * A Resource class responsible for handling CRUD operations on Group objects.
 */
@Path(value = "/group")
public class GroupController {

    private final GroupService groupService = GroupServiceImpl.getInstance();

    /**
     * Handles a HTTP POST request to create a new group.
     *
     * @param group object decoded from the payload of POST request
     * @return a response indicating the outcome of the request
     */
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createGroup(Group group) {
        if (group != null) {
            groupService.addGroup(group);
            return Response.ok().entity("success").build();
        } else {
            return Response.status(409).build();
        }
    }

    /**
     * Handles a HTTP GET request to find group.
     *
     * @param groupName object decoded from the payload of PUT request
     * @return a response indicating the outcome of the request
     */
    @GET
    @Path("/find/{groupName}")
    @Produces("application/json")
    public Group findGroup(@PathParam("groupName") String groupName) {
        if (!groupName.equals("")) {
            return groupService.findGroup(groupName);
        } else {
            return null;
        }
    }

    /**
     * Handles a HTTP DELETE request to delete a group.
     *
     * @param groupName string decoded from the path of DELETE request
     * @return a response indicating the outcome of the request
     */
    @DELETE
    @Path("/delete/{groupName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteGroup(@PathParam("groupName") String groupName, User user) {
        return groupService.removeGroup(groupName, user);
    }

    /**
     * Handles a HTTP PUT request to add member to the group.
     *
     * @param user      object decoded from the payload of PUT request
     * @param groupName object decoded from the payload of PUT request
     * @return a response indicating the outcome of the request
     */
    @PUT
    @Path("/add-member/{groupName}/member/{memberName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMembers(@PathParam("groupName") String groupName,
                               @PathParam("memberName") String memberName,
                               User user) {
        return groupService.addMembers(groupName, memberName, user);
    }

    /**
     * Handles a HTTP PUT request to add moderator to the group.
     *
     * @param moderator object decoded from the payload of PUT request
     * @return a response indicating the outcome of the request
     */
    @PUT
    @Path("/add-moderator/{groupName}/moderator/{moderatorName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addModerator(@PathParam("groupName") String groupName,
                                 @PathParam("moderatorName") String moderatorName, User moderator) {
        return groupService.addModerator(groupName, moderatorName, moderator);

    }

    /**
     * Handles a HTTP DELETE request to remove member from the group.
     *
     * @param user       username of logged in user
     * @param groupName  object decoded from the payload of PUT request
     * @param memberName username of member to be added
     * @return a response indicating the outcome of the request
     */
    @DELETE
    @Path("/remove-member/{groupName}/member/{memberName}")
    public Response removeMember(@PathParam("groupName") String groupName,
                                 @PathParam("memberName") String memberName,
                                 User user) {
        return groupService.removeMember(groupName, memberName, user);
    }

    /**
     * Handles a HTTP DELETE request to remove moderator from the group.
     *
     * @param moderator object decoded from the payload of DELETE request
     * @return a response indicating the outcome of the request
     */
    @DELETE
    @Path("/remove-moderator/{groupName}/moderator/{moderatorName}")
    public Response removeModerator(@PathParam("groupName") String groupName,
                                    @PathParam("moderatorName") String moderatorName, User moderator) {
        return groupService.removeModerator(groupName, moderatorName, moderator);
    }

    /**
     * Handles a HTTP GET request to fetch members of group.
     *
     * @param groupName object decoded from the payload of GET request
     * @return a response indicating the outcome of the request
     */
    @GET
    @Path("/find/members/{groupName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public Response findGroupMembers(@PathParam("groupName") String groupName) {
        return groupService.getGroupMembers(groupName);
    }

    /**
     * Handles a HTTP GET request to find moderators of the group.
     *
     * @param groupName object decoded from the payload of GET request
     * @return a response indicating the outcome of the request
     */
    @GET
    @Path("/find/moderators/{groupName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public Response findGroupModerators(@PathParam("groupName") String groupName) {
        return groupService.getGroupModerators(groupName);
    }

    @PUT
    @Path("/merge/{groupA}/{groupB}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response mergeGroups(@PathParam("groupA") String groupA,
                                @PathParam("groupB") String groupB, User moderator) {
        return groupService.mergeGroups(groupA, groupB, moderator);
    }

    @POST
    @Path("/authenticate")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response checkPasswordForGroup(Group group) {
        try {
            Group dbGroup = groupService.findGroup(group.getGroupName());

            if (dbGroup.getPassword().equals(group.getPassword())) {
                return Response.ok().build();
            } else {
                // Incorrect password
                return Response.status(401).build();
            }
        } catch (NullPointerException e) {
            // Invalid groupName or group does not exist at all.
            return Response.status(401).build();
        }
    }
}
