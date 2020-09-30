package com.neu.prattle.websocket;

/**
 * A simple chat client based on websockets.
 *
 * @author https://github.com/eugenp/tutorials/java-websocket/src/main/java/com/baeldung/websocket/ChatEndpoint.java
 * @version dated 2017-03-05
 */

import com.neu.prattle.model.Group;
import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;
import com.neu.prattle.service.*;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * This class represents a ChatEndpoint object. It handles MessageContents
 * that arrive on the server.
 */
@ServerEndpoint(value = "/chat/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ChatEndpoint {
    // the Constant chatEndpoints
    private static final Set<ChatEndpoint> chatEndpoints = new CopyOnWriteArraySet<>();
    // online users
    private static final HashMap<String, String> onlineUsers = new HashMap<>();
    // the user service
    private UserService accountService = UserServiceImpl.getInstance();
    // the group service
    private ChatAndMessageService chatAndMessageService = ChatAndMessageServiceImpl.getInstance();
    // the group service
    private GroupService groupService = GroupServiceImpl.getInstance();
    // the session
    private Session session;

    /**
     * Broadcast.
     * <p>
     * Send a Message to each session in the pool of sessions.
     * The Message sending action is synchronized.  That is, if another
     * Message tries to be sent at the same time to the same endpoint,
     * it is blocked until this Message finishes being sent..
     *
     * @param message
     */
    private static void broadcast(Message message) {
        for (ChatEndpoint endpoint : chatEndpoints) {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote()
                            .sendObject(message);
                } catch (IOException | EncodeException e) {
                    /* note: in production, who exactly is looking at the console.  This exception's
                     *       output should be moved to a logger.
                     */
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Private method to find a user among open sessions.
     *
     * @param username of the user to find
     * @return the chat end point
     */
    private static ChatEndpoint getChatEndPoint(String username) {
        Optional<String> optionalSessionKey = onlineUsers.entrySet()
                .stream()
                .filter(entry -> username.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst();
        String sessionKey = null;
        if (optionalSessionKey.isPresent()) {
            sessionKey = optionalSessionKey.get();
        }

        String finalSessionKey = sessionKey;
        Optional<ChatEndpoint> optionalChatEndpoint = chatEndpoints.stream()
                .filter(chatEndpoint -> chatEndpoint.session.getId().equals(finalSessionKey))
                .findFirst();

        ChatEndpoint chatEndpoint = null;

        if (optionalChatEndpoint.isPresent()) {
            chatEndpoint = optionalChatEndpoint.get();
        }

        return chatEndpoint;
    }

    /**
     * On open.
     * <p>
     * Handles opening a new session (websocket connection). If the user is a known
     * user (user management), the session added to the pool of sessions and an
     * announcement to that pool is made informing them of the new user.
     * <p>
     * If the user is not known, the pool is not augmented and an error is sent to
     * the originator.
     *
     * @param session  the web-socket (the connection)
     * @param username the name of the user (String) used to find the associated
     *                 UserService object
     * @throws IOException     Signals that an I/O exception has occurred.
     * @throws EncodeException the encode exception
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {
        User user = accountService.findUser(username);

        if (user.getUsername().equals(username)) {
            addEndpoint(session, user.getUsername());
            Message message = userIsOnlineMessage(username);
            broadcast(message);
        }
    }

    /**
     * Creates a Message that some user is now connected - that is, a Session was opened
     * successfully.
     *
     * @param username the username
     * @return a success message
     */
    private Message userIsOnlineMessage(String username) {
        return Message.messageBuilder()
                .setFrom(username)
                .setMessageContent("CONNECTED")
                .setMessageTimeStamp()
                .build();
    }

    /**
     * Adds a newly opened session to the pool of sessions.
     *
     * @param session  the newly opened session
     * @param username the user who connected
     */
    private void addEndpoint(Session session, String username) {
        this.session = session;
        onlineUsers.put(session.getId(), username);
        chatEndpoints.add(this);
    }

    /**
     * On message.
     * <p>
     * When a message arrives, broadcast it to all connected users.
     *
     * @param session the session originating the message
     * @param message the text of the inbound message
     */
    @OnMessage
    public void onMessageContent(Session session, Message message) {
        message.setTimeStamp();

        String type = message.getType();

        switch (type) {
            case "USER":
                sendPersonalMessageContent(message, message.getTo(), true);
                break;
            case "GROUP":
                sendGroupMessageContent(message, message.getTo());
                break;
            default:
                break;
        }
    }

    /**
     * On close.
     * <p>
     * Closes the session by removing it from the pool of sessions and
     * broadcasting the news to everyone else.
     *
     * @param session the session
     */
    @OnClose
    public void onClose(Session session) {
        chatEndpoints.remove(this);
        Message message = new Message();
        message.setFrom(onlineUsers.get(session.getId()));
        message.setContent("DISCONNECTED");
        message.setTimeStamp();
        broadcast(message);
    }

    /**
     * On error.
     * <p>
     * Handles situations when an error occurs.  Not implemented.
     *
     * @param session   the session with the problem
     * @param throwable the action to be taken.
     */
    @OnError
    public void onError(Session session, Throwable throwable) {

        // Do error handling here
    }

    /**
     * Private method to send message to individual users.
     *
     * @param message  to send
     * @param username
     */
    private void sendPersonalMessageContent(Message message, String username, boolean includeSender) {
        try {
            ChatEndpoint endpoint = getChatEndPoint(username);
            if (includeSender) {
                this.session.getBasicRemote().sendObject(message);
            }
            try {
                endpoint.session.getBasicRemote().sendObject(message);
            } catch (NullPointerException e) {
                // do nothing
            }
        } catch (IOException | EncodeException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Private method to send a group message.
     *
     * @param message       to send
     * @param groupUsername of the group receiving the message
     */
    private void sendGroupMessageContent(Message message, String groupUsername) {
        String senderUsername = onlineUsers.get(this.session.getId());
        User sender = accountService.findUser(senderUsername);
        Group group = groupService.findGroup(groupUsername);

        Message errorMessage = new Message();
        errorMessage.setFrom(onlineUsers.get(session.getId()));
        errorMessage.setContent("Illegal! You have to be a part of this group to send a group message.");

        if (!group.getListOfUsers().contains(sender)) {
            sendPersonalMessageContent(errorMessage, senderUsername, false);
        } else {
            for (User user : group.getListOfUsers()) {
                sendPersonalMessageContent(message, user.getUsername(), false);
            }
        }
    }
}
