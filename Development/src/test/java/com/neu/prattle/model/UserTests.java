package com.neu.prattle.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for User class.
 */
public class UserTests {

    private User user1;
    private User user2;
    private User user3;

    /**
     * Test object set up.
     */
    @Before
    public void setUp() {
        user1 = new User();
        user2 = new User();
        user3 = new User("alecherry", "password", "Alessia",
                "Pizzoccheri", "Italian");
    }

    /**
     * Tests for objects constructors.
     */
    @Test
    public void testConstructors() {
        Assert.assertNotNull(user1);
        Assert.assertTrue(user1 instanceof User);
        Assert.assertNotNull(user2);
        Assert.assertTrue(user2 instanceof User);
    }

    /**
     * Tests for objects setters/getters methods.
     */
    @Test
    public void testMethods() {
        Assert.assertNull(user1.getUsername());
        user2.setUsername("sanju214");
        Assert.assertEquals("sanju214", user2.getUsername());
        user1.setUsername("alexp1");
        Assert.assertEquals("alexp1", user1.getUsername());
        user1.setLastName("Pho");
        Assert.assertEquals("Pho", user1.getLastName());
        user1.setFirstName("Alex");
        Assert.assertEquals("Alex", user1.getFirstName());
        user1.setPassword("123");
        Assert.assertEquals("123", user1.getPassword());
        user2.setUsername("@lessia");
        Assert.assertEquals("@lessia", user2.getUsername());
        User user4 = user1;
        Assert.assertTrue(user1.equals(user4));
        Assert.assertFalse(user1.equals(user2));
        Assert.assertEquals(-1415071618, user1.hashCode());
        Assert.assertEquals("Italian", user3.getPrimaryLanguage());
        Assert.assertEquals("password", user3.getPassword());
        Assert.assertEquals("Alessia", user3.getFirstName());
        Assert.assertEquals("Pizzoccheri", user3.getLastName());
        Assert.assertFalse(user2.equals(user3));
        Assert.assertNull(user2.getPrimaryLanguage());
        Assert.assertNull(user2.getPassword());
        Assert.assertNull(user2.getFirstName());
        Assert.assertNull(user2.getLastName());
        Assert.assertFalse(user1.equals(new Object()));
    }
}
