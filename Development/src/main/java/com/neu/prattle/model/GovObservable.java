package com.neu.prattle.model;

import com.mongodb.MongoChangeStreamException;
import com.mongodb.MongoCursorNotFoundException;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import com.mongodb.client.model.changestream.OperationType;
import com.neu.prattle.db.MongoDBChat;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.singletonList;

/**
 * Using Mongo Change streams we are able to observe all changes to the following collections
 * Chat Logs
 * Message Logs
 * This class will allow us to alert the observer of any changes in content for a particular user
 * when a thread/message or chat has been updated.
 */
public class GovObservable {

    private Message message;
    private Document document;

    private MongoDBChat dbMessages = new MongoDBChat();
    private MongoCursor<ChangeStreamDocument<Document>> messageCursor;

    private PropertyChangeSupport support;

    public GovObservable() {
        support = new PropertyChangeSupport(this);
    }

    private void listen(String username) {
        // Get a match of all the documents in the message log collection so we can iterate through them.
        String userToMatch = "{'fullDocument.from': " + "'" + username + "'" + "}";
        List<Bson> msgPipeline = singletonList(Aggregates.match(Filters.or(
                Document.parse(userToMatch))));

        try {
            messageCursor = dbMessages.getMessageCollection().watch(msgPipeline).fullDocument(FullDocument.UPDATE_LOOKUP).iterator();

            while (messageCursor.getServerCursor() != null) {
                ChangeStreamDocument<Document> nextMsg = messageCursor.tryNext();
                if (nextMsg != null
                        && nextMsg.getOperationType() == OperationType.INSERT) {
                    Document oldDocument = this.document;
                    this.document = nextMsg.getFullDocument();
                    System.out.println(this.document);
                    support.firePropertyChange("document", oldDocument, this.document);
                }
            }
        } catch (MongoChangeStreamException e) {
            throw new MongoChangeStreamException(e.getMessage());
        } catch (MongoCursorNotFoundException e) {
            throw new MongoCursorNotFoundException(Objects.requireNonNull(messageCursor.getServerCursor()).getId(), messageCursor.getServerAddress());
        } catch (MongoException e) {
            throw new MongoException(e.getMessage());
        }
    }

    /**
     * Used to add an observer.
     *
     * @param changeListener Tracks the observer to the observerable
     * @param userToObserve  The username of the user to observe
     */
    public void addPropertyChangeListener(PropertyChangeListener changeListener, String userToObserve) {
        support.addPropertyChangeListener(changeListener);
//        listen(userToObserve);
    }

    /**
     * Removes an observer from an observerable user.
     *
     * @param changeListener The observer.
     */
    public void removePropertyChangeListener(PropertyChangeListener changeListener) {
        support.removePropertyChangeListener(changeListener);
        messageCursor.close();
        this.message = null;
    }

    /**
     * Getter used for manual testing to confirm observer's message content.
     *
     * @return Message payload passed from the observerable to the observer.
     */
    public String getMessage() {
        return message.getContent();
    }

    /**
     * Setter used for manual testing, disable the listen() method if using this.
     *
     * @param newMessage Message payload to trigger the observer.
     */
    public void setMessage(Message newMessage) {
        Message oldMessage = this.message;
        this.message = newMessage;
        support.firePropertyChange("message", oldMessage, newMessage);
    }
}
