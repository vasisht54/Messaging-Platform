package com.neu.prattle.controller;

import com.neu.prattle.model.Message;

/**
 * A POJO class to store Login credentials sent
 * via a {@POST} request.
 */
class MessageParser {
    private String chatID;
    private Message message;

    /**
     * Getter method for the chatID.
     *
     * @return the chatID of the message
     */
    public String getID() {
        return chatID;
    }

    /**
     * Setter method for the chatID of the message.
     *
     * @param chatID of the message
     */
    public void setID(String chatID) {
        this.chatID = chatID;
    }

    /**
     * Getter method for the password.
     *
     * @return the message
     */
    public Message getMessage() {
        return message;
    }

    /**
     * Setter method for the message.
     *
     * @param message to store in the database
     */
    public void setMessage(Message message) {
        this.message = message;
    }
}
