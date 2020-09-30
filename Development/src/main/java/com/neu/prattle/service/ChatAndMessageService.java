package com.neu.prattle.service;

import com.neu.prattle.model.Message;
import org.bson.Document;

import java.util.List;

/**
 * This interface represents a ChatAndMessageService object; it includes
 * all the methods available to classes who implement the interface.
 */
public interface ChatAndMessageService {

    /**
     * Add a chat to the database with a unique chatID. In this method,
     * we assume the chat is between two users.
     *
     * @param one   user belonging to the conversation
     * @param other user belonging to the conversation
     */
    void addChat(String one, String other);

    /**
     * Add a chat to the database.
     *
     * @param groupID of the group chat
     */
    void addChat(String groupID);

    /**
     * Given two usernames, find the chat with the correct participants ID.
     *
     * @param one   user belonging to the conversation
     * @param other user belonging to the conversation
     * @return the chat participants ID in astring
     */
    String findChat(String one, String other);

    /**
     * Given a chat participants ID, find the chat in the database.
     *
     * @param chatID of the group chat
     * @return the chat participants ID in a string
     */
    String findChat(String chatID);

    /**
     * Given a chat participants ID, add a message to the database
     * with a chat ID.
     *
     * @param chatID  of the chat the message belongs to
     * @param message to be added
     */
    String addMessage(String chatID, Message message);

    /**
     * Given a message ID, add a thread field to the message.
     *
     * @param messageID of the message
     */
    void addThread(String messageID);

    /**
     * Add a message to the database.
     *
     * @param chatID of the chat the message belongs to
     * @return a list of messages
     */
    List<String> findMessages(String chatID);

    /**
     * Given a message ID, find the message in the database.
     *
     * @param messageID of the chat the message belongs to
     * @return the message in a string
     */
    Document findMessage(String messageID);

    /**
     * Given a chat participants ID, add a message to the database
     * with a chat ID.
     *
     * @param messageID of the message
     * @param message   to be added to the thread
     */
    void addMessageToThread(String messageID, Message message);

    /**
     * Add a new message to the pool of existing messages.
     *
     * @param participants to be added
     */
    void deleteChat(String participants);

    /**
     * Add a new message to the pool of existing messages.
     *
     * @param participants to be added
     */
    void deleteMessages(String participants);

    /**
     * Given a message ID, delete the message in the database.
     *
     * @param messageID of the chat the message belongs to
     */
    void deleteMessage(String messageID);
}
