package com.neu.prattle.db;

import com.mongodb.client.MongoIterable;
import org.bson.Document;

/**
 * This interface represents the methods for Database operations
 * related to messaging.
 */
public interface DatabaseChat {

    /**
     * Add a new conversation to the database using a unique id.
     *
     * @param chat object to be added
     */
    void addChat(Document chat);

    /**
     * Add a new message to the database.
     *
     * @param message to be added
     */
    String addMessage(Document message);

    /**
     * Add a thread field to a message.
     *
     * @param messageID of the message
     */
    void addThread(String messageID);

    /**
     * Find a conversation within the database using its participants id.
     *
     * @param participants of the conversation
     * @return the chat in a Document
     */
    Document findChat(String participants);

    /**
     * Given a chat ID, find all of messages who have the same chat ID.
     *
     * @param chatID of the conversation the messages belong to
     * @return an iterable collection of messages in a Document
     */
    MongoIterable<Document> findMessages(String chatID);

    /**
     * Given a message ID, find the message with that ID.
     *
     * @param messageID of the message to find
     * @return a document containing the message
     */
    Document findMessage(String messageID);

    /**
     * Given a message ID and a message Document, add the document
     * to the message thread.
     *
     * @param messageID of the message to find
     * @param message   to add to the thread
     */
    void addMessageToThread(String messageID, Document message);

    /**
     * Given a participants ID, delete the chat from the database.
     *
     * @param participants of the conversation
     */
    void deleteChat(String participants);

    /**
     * Delete a collection of messages belonging to the same conversation.
     *
     * @param chatID of the conversation the messages belong to
     */
    void deleteMessages(String chatID);

    /**
     * Given a message ID, delete the message from thr databae.
     *
     * @param messageID of the message to find
     */
    void deleteMessage(String messageID);
}
