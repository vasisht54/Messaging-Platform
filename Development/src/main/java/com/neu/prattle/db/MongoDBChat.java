package com.neu.prattle.db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;

/**
 * This class provides the connection to the mongo server.
 */
public class MongoDBChat implements DatabaseChat {
    private static DBConnection conn = new DBConnection();
    private MongoCollection<Document> messages;
    private MongoCollection<Document> chats;
    private String CHAT_ID = "chat_id";
    private String PARTICIPANTS = "participants";
    private String OBJECT_ID = "_id";

    /**
     * Class constructor; instantiates the database connection with a predefined connection String
     */

    public MongoDBChat() {
        Properties properties = new Properties();
        try {
            properties.load(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("mongodb.properties")));
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }

        String dbConnectionName = properties.getProperty("db.collection.name");
        String dbConnectionMessages = properties.getProperty("db.collection.messages");
        String dbConnectionChats = properties.getProperty("db.collection.chats");

        MongoDatabase db = conn.getConnection().getDatabase(dbConnectionName);
        this.messages = db.getCollection(dbConnectionMessages);
        this.chats = db.getCollection(dbConnectionChats);
    }

    /**
     * Add a new conversation to the database using a unique id.
     *
     * @param chat object to be added
     */
    public void addChat(Document chat) {
        chats.insertOne(chat);
    }

    /**
     * Add a new message to the database.
     *
     * @param message to be added
     */
    public String addMessage(Document message) {
        messages.insertOne(message);
        return message.getObjectId(OBJECT_ID).toString();
    }

    /**
     * Find a conversation within the database using its participants id.
     *
     * @param participants of the conversation
     * @return the chat in a Document
     */
    public Document findChat(String participants) {
        return chats.find(eq(PARTICIPANTS, participants)).first();
    }

    /**
     * Given a chat ID, find all of messages who have the same chat ID.
     *
     * @param chatID of the conversation the messages belong to
     * @return an iterable collection of messages in a Document
     */
    public MongoIterable<Document> findMessages(String chatID) {
        // return all messages with given id
        return messages.find(eq(CHAT_ID, chatID));
    }

    /**
     * Given a message ID, find the message with that ID.
     *
     * @param messageID of the message to find
     * @return a document containing the message
     */
    public Document findMessage(String messageID) {
        // Document all messages with given id
        return messages.find(eq(OBJECT_ID, new ObjectId(messageID))).first();
    }

    /**
     * Given a message ID and a message Document, add the document
     * to the message thread.
     *
     * @param messageID of the message to find
     */
    public void addThread(String messageID) {
        Document updated = checkIfThreadExists(findMessage(messageID));
        deleteMessage(messageID);
        addMessage(updated);
    }

    /**
     * Private method to check if a message already has a thread.
     *
     * @param doc to add the thread to
     * @return the updated message
     */
    private Document checkIfThreadExists(Document doc) {
        String threadField = "thread";
        List<Document> thread = (List<Document>) doc.get(threadField);

        try {
            if (!thread.isEmpty()) {
                return doc;
            }
        } catch (NullPointerException e) {
            thread = new ArrayList<>();
            doc.append(threadField, thread);
        }

        return doc;
    }

    /**
     * Given a message ID and a message Document, add the document
     * to the message thread.
     *
     * @param messageID of the message to find
     * @param message   to add to the thread
     */
    public void addMessageToThread(String messageID, Document message) {
        Document doc = findMessage(messageID);
        List<Document> thread = (List<Document>) doc.get("thread");
        thread.add(message);
        deleteMessage(messageID);
        addMessage(doc);
    }

    /**
     * Given a participants ID, delete the chat from the database.
     *
     * @param participants of the conversation
     */
    public void deleteChat(String participants) {
        chats.deleteOne(eq(PARTICIPANTS, participants));
    }

    /**
     * Delete a collection of messages belonging to the same conversation.
     *
     * @param chatID of the conversation the messages belong to
     */
    public void deleteMessages(String chatID) {
        // return all messages with given id
        messages.deleteMany(eq(CHAT_ID, chatID));
    }

    /**
     * Given a message ID, delete the message from thr databae.
     *
     * @param messageID of the message to find
     */
    public void deleteMessage(String messageID) {
        // return all messages with given id
        messages.deleteOne(new Document(OBJECT_ID, new ObjectId(messageID)));
    }

    public MongoCollection<Document> getMessageCollection() {
        return messages;
    }

    public MongoCollection<Document> getChatCollection() {
        return chats;
    }
}
