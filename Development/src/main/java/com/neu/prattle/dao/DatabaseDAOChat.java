package com.neu.prattle.dao;

import com.mongodb.client.MongoIterable;
import com.neu.prattle.db.DatabaseChat;
import com.neu.prattle.db.MongoDBChat;
import org.bson.Document;

/**
 * This class represents a DatabaseDAO to manage the chat and message database.
 */
public class DatabaseDAOChat {
    private static DatabaseChat db = new MongoDBChat();

    /**
     * Add a new chat to the pool of existing chats.
     *
     * @param chat to be added
     */
    public void createChat(Document chat) {
        db.addChat(chat);
    }

    /**
     * Add a new message to the pool of existing messages.
     *
     * @param message to be added
     */
    public String createMessage(Document message) {
        db.addMessage(message);

        return message.get("_id").toString();
    }

    /**
     * Given a chat's participants ID, find the chat in the database.
     *
     * @param participants to be added
     * @return the participants ID
     */
    public String readChat(String participants) {
        String str;

        try {
            str = db.findChat(participants).get("participants").toString();
        } catch (NullPointerException e) {
            str = "";
        }

        return str;
    }

    /**
     * Given a message chat ID, find and return all messages with the
     * given chatID.
     *
     * @param chatID of the messages to be found
     * @return an iterable collection of messages
     */
    public MongoIterable<Document> readMessages(String chatID) {
        return db.findMessages(chatID);
    }

    /**
     * Given a message ID, find the message with that ID.
     *
     * @param messageID of the message to find
     * @return a document containing the message
     */
    public Document readMessage(String messageID) {
        return db.findMessage(messageID);
    }

    /**
     * Given a message ID, add a thread to that message.
     *
     * @param messageID of the message whose thread we are adding to
     */
    public void addThread(String messageID) {
        db.addThread(messageID);
    }

    /**
     * Given a message ID, add a message to another message's thread.
     *
     * @param messageID of the message whose thread we are adding to
     * @param message   to add to the thread
     */
    public void addMessageToThread(String messageID, Document message) {
        db.addMessageToThread(messageID, message);
    }

    /**
     * Given a participants ID, delete the chat from the database.
     *
     * @param participants to be added
     */
    public void deleteChat(String participants) {
        db.deleteChat(participants);
    }

    /**
     * Given a chat ID, delete all messages with the ID from the database.
     *
     * @param chatID of the messages to be deleted
     */
    public void deleteMessages(String chatID) {
        db.deleteMessages(chatID);
    }

    /**
     * Given a message ID, delete the message from thr databae.
     *
     * @param messageID of the message to find
     */
    public void deleteMessage(String messageID) {
        db.deleteMessage(messageID);
    }
}
