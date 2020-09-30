package com.neu.prattle.controller;

import com.neu.prattle.model.Message;
import com.neu.prattle.service.ChatAndMessageService;
import com.neu.prattle.service.ChatAndMessageServiceImpl;
import org.bson.Document;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * A Resource class responsible for handling CRUD operations
 * on User objects.
 *
 * @author CS5500 Fall 2019 Teaching staff
 * @version dated 2019-10-06
 */
@Path("/message")
public class MessageController {
    // Usually Dependency injection will be used to inject the service at run-time
    private ChatAndMessageService chatAndMessageService = ChatAndMessageServiceImpl.getInstance();

    /**
     * Handles a HTTP POST request to sign up as a new user.
     *
     * @param chatID object decoded from the payload of POST request
     * @return a response indicating the outcome of the request
     */
    @GET
    @Path("/{chatID}")
    @Produces("application/json")
    public List<String> findMessageHistory(@PathParam("chatID") String chatID) {
        System.out.println(chatID);
        // try adding a new user
        List<String> history = chatAndMessageService.findMessages(chatID);

        if (history.isEmpty()) {
            return new ArrayList<>();
        }

        return history;
    }

    /**
     * Handles a HTTP POST request to add a message to the database.
     *
     * @param chatID  of the chat the message belongs to
     * @param message holding the messageID of the message to delete
     * @return a string containing the message ID
     */
    @POST
    @Path("/add/message/{chatID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public String addMessage(@PathParam("chatID") String chatID, Message message) {
        message.setTimeStamp();
        // try adding a new user
        String messageID = "{\"id\":\"" + chatAndMessageService.addMessage(chatID, message) + "\"}";

        return messageID;
    }

    /**
     * Handles a HTTP POST request to add a message to the database.
     *
     * @param chatID of the chat to be added
     * @return a response indicating the outcome of the request
     */
    @POST
    @Path("/add/chat")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addChat(SingleString chatID) {
        chatAndMessageService.addChat(chatID.getValue());

        return Response.ok().build();
    }

    /**
     * Handles a HTTP POST request to add a message to the database.
     *
     * @param message holding the messageID of the message to delete
     * @return a response indicating the outcome of the request
     */
    @POST
    @Path("/{messageID}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMessageToThread(@PathParam("messageID") String messageID, Message message) {
        message.setTimeStamp();
        chatAndMessageService.addMessageToThread(messageID, message);

        return Response.ok().build();
    }

    /**
     * Handles a HTTP POST request to add a thread to the database.
     *
     * @param messageID of the message whose thread was started.
     * @return a response indicating the outcome of the request
     */
    @POST
    @Path("/add/thread")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addThread(SingleString messageID) {
        chatAndMessageService.addThread(messageID.getValue());

        return Response.ok().build();
    }

    /**
     * Handles a HTTP GET request to find a message in the database.
     *
     * @param messageID of the message to find
     * @return the message in a string
     */
    @GET
    @Path("/find/thread/{messageID}")
    @Produces("application/json")
    public List<String> findThread(@PathParam("messageID") String messageID) {
        List<Document> docs = (List<Document>) chatAndMessageService.findMessage(messageID).get("thread");
        List<String> messages = new ArrayList<>();

        try {
            for (Document doc : docs) {
                messages.add(doc.toJson());
            }
        } catch (NullPointerException e) {
            // do nothing
        }
        return messages;
    }

    /**
     * Handles a HTTP POST request to delete a message from the database.
     *
     * @param str holding the messageID of the message to delete
     * @return a response indicating the outcome of the request
     */
    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteMessage(SingleString str) {
        // try adding a new user
        chatAndMessageService.deleteMessage(str.getValue());

        return Response.ok().build();
    }
}
