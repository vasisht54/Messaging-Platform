package com.neu.prattle.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A Basic POJO for Message.
 *
 * @author CS5500 Fall 2019 Teaching staff
 * @version dated 2019-10-06
 */
public class Message {
    // the name of the user who sent this message
    private String type;
    private boolean isPrivate;
    // the name of the user who sent this message
    private String from;
    // the name of the user to whom the message is sent.
    private String to;
    // the contents of the message
    private String content;
    private Date timeStamp;
    private List<Message> thread = new ArrayList<>();

    /**
     * Method to craft a new message.
     *
     * @return a new message builder
     */
    public static MessageBuilder messageBuilder() {
        return new MessageBuilder();
    }

    /**
     * Return the sender, receiver and content of the message
     * in a string.
     *
     * @return class fields in a string
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("\nType: ").append(type);
        str.append("\nFrom: ").append(from);
        str.append("\nTo: ").append(to);
        str.append("\nContent: ").append(content);
        str.append("\nSent: ").append(getTimeStamp());

        return str.toString();
    }

    /**
     * Getter method to return the sender's name.
     *
     * @return the sender
     */
    public String getType() {
        return type;
    }

    /**
     * Setter method for sender's name.
     *
     * @param type the name of the sender
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter method to return the message's status.
     *
     * @return the sender
     */
    public boolean getStatus() {
        return isPrivate;
    }

    /**
     * Setter method for message's status.
     *
     * @param status of the message
     */
    public void setStatus(boolean status) {
        this.isPrivate = status;
    }

    /**
     * Getter method to return the sender's name.
     *
     * @return the sender
     */
    public String getFrom() {
        return from;
    }

    /**
     * Setter method for sender's name.
     *
     * @param from the name of the sender
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Getter method returning the receiver's name.
     *
     * @return the receiver
     */
    public String getTo() {
        return to;
    }

    /**
     * Setter method for receiver's name.
     *
     * @param to the name of the receiver
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Getter method returning the receiver's name.
     *
     * @return the receiver
     */
    public String getContent() {
        return content;
    }

    /**
     * Setter method for message's content.
     *
     * @param content of the message
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Getter method returning the message's time stamp.
     *
     * @return the time stamp
     */
    public String getTimeStamp() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

        return dateFormatter.format(timeStamp);
    }

    /**
     * Setter method for message's time stamp.
     */
    public void setTimeStamp() {
        this.timeStamp = new Date();
    }

    /**
     * Getter method returning the message's time stamp.
     *
     * @return the time stamp
     */
    public List<Message> getThread() {
        return thread;
    }

    /**
     * A Builder helper class to create instances of {@link Message}.
     */
    public static class MessageBuilder {
        // Invoking the build method will return this message object.
        Message message;

        /**
         * Class constructor.
         */
        public MessageBuilder() {
            message = new Message();
            message.setFrom("Not set");
        }

        /**
         * Setter method for sender's name.
         *
         * @param type the name of the sender
         */
        public MessageBuilder setType(String type) {
            message.setType(type);
            return this;
        }

        /**
         * Setter method for sender's name.
         *
         * @param from the name of the sender
         */
        public MessageBuilder setFrom(String from) {
            message.setFrom(from);
            return this;
        }

        /**
         * Setter method for receiver's name.
         *
         * @param to the name of the receiver
         */
        public MessageBuilder setTo(String to) {
            message.setTo(to);
            return this;
        }

        /**
         * Setter method for message's content.
         *
         * @param content of the message
         */
        public MessageBuilder setMessageContent(String content) {
            message.setContent(content);
            return this;
        }

        /**
         * Setter method for message's time stamp.
         */
        public MessageBuilder setMessageTimeStamp() {
            message.setTimeStamp();
            return this;
        }

        /**
         * Return message object.
         *
         * @return the message object
         */
        public Message build() {
            return message;
        }
    }
}
