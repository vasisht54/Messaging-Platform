package com.neu.prattle.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for Message class.
 */
public class MessageTests {
    private Message test;
    private Message.MessageBuilder test2;

    /**
     * Test object set up.
     */
    @Before
    public void setUp() {
        test = new Message();
        test2 = new Message.MessageBuilder();
    }

    /**
     * Tests for objects constructors.
     */
    @Test
    public void testConstructors() {
        Assert.assertNotNull(test);
        Assert.assertTrue(test instanceof Message);
        Assert.assertNotNull(test2);
        Assert.assertTrue(test2 instanceof Message.MessageBuilder);
        Assert.assertTrue(test2.build() instanceof Message);
        Assert.assertNotNull(new Message.MessageBuilder());
        Assert.assertTrue(test2.build() instanceof Message);
    }

    /**
     * Tests for objects setters/getters methods.
     */
    @Test
    public void testMethods() {
        test.setType("User");
        Assert.assertEquals("User", test.getType());
        Assert.assertFalse(test.getStatus());
        test.setStatus(true);
        Assert.assertTrue(test.getStatus());
        test.setFrom("alessia123");
        Assert.assertEquals("alessia123", test.getFrom());
        Assert.assertNotEquals("Alessia123", test.getTo());
        test.setTo("amit_dev");
        Assert.assertEquals("amit_dev", test.getTo());
        Assert.assertNotEquals("amit__dev", test.getTo());
        test.setContent("Hello, there!");
        Assert.assertEquals("Hello, there!", test.getContent());
        test.setTimeStamp();
        Assert.assertEquals("\n" +
                "Type: User" +
                "\n" +
                "From: alessia123" +
                "\n" +
                "To: amit_dev" +
                "\n" +
                "Content: Hello, there!" +
                "\n" +
                "Sent: " +
                test.getTimeStamp(), test.toString());
        test.setFrom("sanjuMan");
        Assert.assertNotEquals("AMIT", test.getTo());
        Assert.assertEquals("sanjuMan", test.getFrom());
        Assert.assertNotNull(Message.messageBuilder());
        Assert.assertNotNull(Message.messageBuilder() instanceof Message.MessageBuilder);
        Assert.assertEquals(0, test.getThread().size());
        test.getThread().add(test);
        Assert.assertEquals(1, test.getThread().size());
    }

    /**
     * Tests for objects setters/getters methods.
     */
    @Test
    public void testStaticClass() {
        test2.setType("user");
        test2.setFrom("alex");
        test2.setTo("amit");
        test2.setMessageContent("How are you?");
        test2.setMessageTimeStamp();
        Assert.assertEquals("\n" +
                "Type: user" +
                "\n" +
                "From: alex" +
                "\n" +
                "To: amit" +
                "\n" +
                "Content: How are you?\nSent: " + test2.build().getTimeStamp(), test2.build().toString());
    }
}
