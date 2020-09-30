package com.neu.prattle.controller;

import com.neu.prattle.exceptions.UserDoesNotExistException;
import com.neu.prattle.model.User;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * A Resource class responsible for handling CRUD operations on User objects.
 *
 * @author CS5500 Fall 2019 Teaching staff
 * @version dated 2019-10-06
 */
@Path(value = "/user")
public class UserController {
    // Usually Dependency injection will be used to inject the service at run-time
    private UserService userService = UserServiceImpl.getInstance();

    /**
     * Handles a HTTP POST request to sign up as a new user.
     *
     * @param user object decoded from the payload of POST request
     * @return a response indicating the outcome of the request
     */
    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUserAccount(User user) {
        // try adding a new user
        try {
            // Returns a user, if not null, a user exists already.
            if (userService.findUser(user.getUsername()) != null) {
                return Response.status(409).build();
            }
        } catch (UserDoesNotExistException e) {
            userService.addUser(user);
        }
        return Response.ok().build();
    }

    /**
     * Handles a HTTP POST request to sign in as an existing loginUser.
     *
     * @param loginUser object decoded from the payload of POST request
     * @return a response indicating the outcome of the request
     */
    @POST
    @Path("/signin")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signInUserAccount(Login loginUser) {
        try {
            // Throws exception if we can't find the user to validate sign in with.
            User dbUser = userService.findUser(loginUser.getUsername());

            if (loginUser.getPassword().equals(dbUser.getPassword())) {
                return Response.ok().build();
            } else {
                // Incorrect password
                return Response.status(401).build();
            }
        } catch (UserDoesNotExistException e) {
            // Invalid username or user does not exist at all.
            return Response.status(401).build();
        }
    }

    /**
     * Handles a HTTP GET request to find the user.
     *
     * @param username of the user to be found, decoded from the payload of POST request
     * @return a response indicating the outcome of the request
     */
    @GET
    @Path("/find/{username}")
    @Produces("application/json")
    public String findUser(@PathParam("username") String username) {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = userService.findUser(username);
        try {
            return objectMapper.writeValueAsString(user);

        } catch (IOException e) {
            return "{}";
        }
    }

    /**
     * Handles a HTTP PUT request to update a user.
     *
     * @return a response indicating the outcome of the request
     */
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(User user) {
        userService.updateUser(user);
        return Response.ok().build();
    }

    /**
     * Handles a HTTP DELETE request to delete an existing user.
     *
     * @param username object decoded from the payload of POST request
     * @return a response indicating the outcome of the request
     */
    @DELETE
    @Path("/delete/{username}")
    public Response deleteUser(@PathParam("username") String username) {
        if (!username.equals("")) {
            try {
                userService.removeUser(username);
                return Response.ok().build();
            } catch (UserDoesNotExistException e) {
                return Response.status(409).build();
            }
        }
        return Response.status(409).build();
    }
}
