package com.neu.prattle.service;

import com.neu.prattle.dao.DatabaseDAOChat;
import com.neu.prattle.exceptions.ChatDoesNotExistException;
import com.neu.prattle.model.Message;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of {@link GroupService}
 * <p>
 * It stores the groups in-memory, which means any groups
 * created will be deleted once the application has been restarted.
 */
public class ChatAndMessageServiceImpl implements ChatAndMessageService {
    private static ChatAndMessageServiceImpl chatAndMessageService;

    static {
        chatAndMessageService = new ChatAndMessageServiceImpl();
    }

    private DatabaseDAOChat db = new DatabaseDAOChat();

    /**
     * Call this method to return an instance of this service.
     *
     * @return this
     */
    public static ChatAndMessageServiceImpl getInstance() {
        return chatAndMessageService;
    }

    /**
     * Add a chat to the database with a unique chatID. In this method,
     * we assume the chat is between two users.
     *
     * @param one   user belonging to the conversation
     * @param other user belonging to the conversation
     */
    public void addChat(String one, String other) {
        String str;
        try {
            // check if chat already exists
            str = findChat(one, other);
        } catch (NullPointerException e) {
            str = "";
        }

        String id = squashUsersToString(one, other);

        // check chat doesn't exist
        if (!str.equals(id)) {
            Document doc = new Document();
            doc.put("participants", id);
            db.createChat(doc);
        }
    }

    /**
     * Private method to hash two usernames into a unique chat
     * participants ID.
     *
     * @param one   of the usernames
     * @param other username
     * @return a string of the unique participants chatID
     */
    private String squashUsersToString(String one, String other) {
        List<String> users = Arrays.asList(one, other);
        // sort users alphabetically
        Collections.sort(users);

        StringBuilder str = new StringBuilder();

        // merge usernames into one string
        users.forEach(str::append);

        return str.toString();
    }

    /**
     * Add a chat to the database.
     *
     * @param groupID of the group chat
     */
    public void addChat(String groupID) {
        String str;
        try {
            // check if chat already exists
            str = findChat(groupID);
        } catch (NullPointerException e) {
            str = "";
        }

        // check chat doesn't exist
        if (!str.equals(groupID)) {
            Document doc = new Document();

            doc.put("participants", groupID);

            db.createChat(doc);
        }
    }

    /**
     * Given two usernames, find the chat with the correct participants ID.
     *
     * @param one   user belonging to the conversation
     * @param other user belonging to the conversation
     * @return the chat participants ID in astring
     */
    public String findChat(String one, String other) {
        return db.readChat(squashUsersToString(one, other));
    }


    /**
     * Given a chat participants ID, find the chat in the database.
     *
     * @param chatID of the group chat
     * @return the chat participants ID in a string
     */
    public String findChat(String chatID) {
        return db.readChat(chatID);
    }


    /**
     * Given a chat participants ID, add a message to the database
     * with a chat ID.
     *
     * @param chatID  of the chat the message belongs to
     * @param message to be added
     */
    public String addMessage(String chatID, Message message) {
        // find the chat in the database
        String str = findChat(chatID);
        // create the document
        Document doc;

        if (str.equals("")) {
            throw new ChatDoesNotExistException("There is not chat matching this id.");
        } else {
            doc = convertMessageToDocument(chatID, message);
            db.createMessage(doc);

        }

        return doc.get("_id").toString();
    }

    /**
     * Private method to convert a Message into a Document.
     *
     * @param message to be converted
     * @return a Document object
     */
    private Document convertMessageToDocument(String chatID, Message message) {
        Document doc = new Document();
        doc.put("chat_id", chatID);
        doc.put("from", message.getFrom());
        doc.put("content", message.getContent());
        doc.put("time_stamp", message.getTimeStamp());
        doc.put("is_private", message.getStatus());

        return doc;
    }

    /**
     * Given a message ID, add a thread field to the message.
     *
     * @param messageID of the message
     */
    public void addThread(String messageID) {

        // find the chat in the database
        db.addThread(messageID);
    }

    /**
     * Given a chat participants ID, add a message to the database
     * with a chat ID.
     *
     * @param messageID of the message
     * @param message   to be added to the thread
     */
    public void addMessageToThread(String messageID, Message message) {
        String chatID = db.readMessage(messageID).get("chat_id").toString();
        Document doc = convertMessageToDocument(chatID, message);

        // find the chat in the database
        db.addMessageToThread(messageID, doc);
    }

    /**
     * Add a message to the database.
     *
     * @param chatID of the chat the message belongs to
     * @return a list of messages
     */
    public List<String> findMessages(String chatID) {
        List<String> messages = new ArrayList<>();
        for (Document doc : db.readMessages(chatID)) {
            messages.add(doc.toJson());
        }

        return messages;
    }

    /**
     * Given a message ID, find the message in the database.
     *
     * @param messageID of the chat the message belongs to
     * @return the message in a string
     */
    public Document findMessage(String messageID) {
        return db.readMessage(messageID);
    }

    /**
     * Add a new message to the pool of existing messages.
     *
     * @param participants to be added
     */
    public void deleteChat(String participants) {
        db.deleteChat(participants);
        deleteMessages(participants);
    }

    /**
     * Add a new message to the pool of existing messages.
     *
     * @param participants to be added
     */
    public void deleteMessages(String participants) {
        db.deleteMessages(participants);
    }

    /**
     * Given a message ID, delete the message in the database.
     *
     * @param messageID of the chat the message belongs to
     */
    public void deleteMessage(String messageID) {
        db.deleteMessage(messageID);
    }
}
