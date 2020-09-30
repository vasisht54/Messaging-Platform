package com.neu.prattle.service;

import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.exceptions.UserDoesNotExistException;
import com.neu.prattle.model.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for UserServiceImplTestSample class.
 */
public class UserServiceImplTests {

    private UserService service;
    private User user1;
    private User user2;

    /**
     * Test object set up.
     */
    @Before
    public void setUp() {
        service = UserServiceImpl.getInstance();
        user1 = new User("testUser1", "password", "Alice",
                "Wonderland", "English");
        user2 = new User("testUser2", "1234", "Alex",
                "Pho", "Japanese");
    }

    /**
     * Test UserAlreadyPresentException.
     */
    @Test(expected = UserAlreadyPresentException.class)
    public void testUserExists() {
        service.addUser(user1);
        service.addUser(user1);
    }

    /**
     * Test NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void testUserNull() {
        service.addUser(null);
    }

    /**
     * Test UserDoesNotExistException.
     */
    @Test(expected = UserDoesNotExistException.class)
    public void testUserDoesNotExists() {
        service.findUser("alessiatest");
    }

    /**
     * Tests for objects methods.
     */
    @Test
    public void testMethods() {
        service.addUser(user2);
        assertEquals("testUser1", service.findUser("testUser1").getUsername());
        assertEquals("testUser2", service.findUser("testUser2").getUsername());
        user1.setFirstName("Alicia");
        service.updateUser(user1);
        assertEquals("Alicia", service.findUser("testUser1").getFirstName());
        service.removeUser(user2.getUsername());
        service.removeUser(user1.getUsername());
        assertNotNull(service.findUsers());
    }
}
