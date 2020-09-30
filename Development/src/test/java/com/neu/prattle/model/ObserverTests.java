package com.neu.prattle.model;

import com.neu.prattle.service.ChatAndMessageService;
import com.neu.prattle.service.ChatAndMessageServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ObserverTests {

    private GovObserver observer;
    private GovObservable observable;

    private Message fromAlex;
    private Message fromAlessia;
    private Message fromJane;
    private Message fromJane2;

    private ChatAndMessageService chatAndMessageService;

    @Before
    public void setUp() {
        observable = new GovObservable();
        observer = new GovObserver();
        chatAndMessageService = ChatAndMessageServiceImpl.getInstance();

        fromAlex = new Message.MessageBuilder()
                .setFrom("alex")
                .setTo("alessia")
                .setType("User")
                .setMessageTimeStamp()
                .setMessageContent("hey!")
                .build();

        fromAlessia = new Message.MessageBuilder()
                .setFrom("alessia")
                .setTo("alex")
                .setType("User")
                .setMessageTimeStamp()
                .setMessageContent("hey back from alessia!")
                .build();

        fromJane = new Message.MessageBuilder()
                .setFrom("janedoe")
                .setTo("alex")
                .setType("User")
                .setMessageTimeStamp()
                .setMessageContent("hey back from janedoe")
                .build();

        fromJane2 = new Message.MessageBuilder()
                .setFrom("janedoe")
                .setTo("alessia")
                .setType("User")
                .setMessageTimeStamp()
                .setMessageContent("hey alessia.")
                .build();
    }

    @Test
    public void oneObserver() {
        Message testMsg = new Message.MessageBuilder()
                .setFrom("alex")
                .setTo("alessia")
                .setType("User")
                .setMessageTimeStamp()
                .setMessageContent("some words in the test")
                .build();

        observable.addPropertyChangeListener(observer, "alex");
        observable.setMessage(testMsg);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        Date now = new Date();
        Assert.assertEquals("some words in the test", observable.getMessage());

        String expected = "\nType: User\n" + "From: alex\n" + "To: alessia\n" + "Content: some words in the test\n"
                + "Sent: " + dateFormatter.format(now);
        Assert.assertEquals(expected, testMsg.toString());
        observer.getAllObservedMessages();
    }

    @Test
    public void twoObservers() {
        GovObserver observer2 = new GovObserver();
        GovObservable observable2 = new GovObservable();


        observable2.addPropertyChangeListener(observer2, "janedoe");

        Message testHere = new Message.MessageBuilder()
                .setFrom("janedoe")
                .setTo("alex")
                .setMessageContent("hey back from janedoe")
                .setMessageTimeStamp()
                .setType("User")
                .build();

        observable2.setMessage(testHere);
        Date now = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

        String expected = "\nType: User\n" + "From: janedoe\n" + "To: alex\n" + "Content: hey back from janedoe\n"
                + "Sent: " + dateFormatter.format(now);
        Assert.assertEquals("hey back from janedoe", observable2.getMessage());

        observable2.setMessage(fromJane2);
        Date now2 = new Date();
        String expected2 = "\nType: User\n" + "From: janedoe\n" + "To: alessia\n" + "Content: hey alessia.\n"
                + "Sent: " + dateFormatter.format(now2);
        Assert.assertEquals("hey alessia.", observable2.getMessage());

        observable2.addPropertyChangeListener(observer, "janedoe");
        observable2.setMessage(fromAlex);
        Date now3 = new Date();
        String expected3 = "\nType: User\n" + "From: alex\n" + "To: alessia\n" + "Content: hey!\n"
                + "Sent: " + dateFormatter.format(now3);
        Assert.assertEquals("hey!", observable2.getMessage());

    }

    @Test(expected = NullPointerException.class)
    public void removeObserver() {

        observable.addPropertyChangeListener(observer, "alex");
        observable.setMessage(fromJane);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        Date now = new Date();
        Assert.assertEquals("hey back from janedoe", observable.getMessage());

        String expected = "\nType: User\n" + "From: janedoe\n" + "To: alex\n" + "Content: hey back from janedoe\n"
                + "Sent: " + dateFormatter.format(now);
        Assert.assertEquals(expected, fromJane.toString());

        observable.removePropertyChangeListener(observer);
        Assert.assertNull(observable.getMessage());
    }

    /**
     * This test needs to be adapted for a blocking environment in that the listener stays open waiting for connections and won't advance / timeout.
     */
//    @Test
//    public void dbTest() {
//        observable.addPropertyChangeListener(observer, "janedoe");
//        chatAndMessageService.addMessage("alicejanedoe", fromJane);
//        Assert.assertEquals("hey back from janedoe", observable.getMessage());
//    }
}
