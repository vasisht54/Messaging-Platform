package com.neu.prattle.db;

import com.neu.prattle.dao.GroupDAOImpl;
import com.neu.prattle.dao.IGroupDAO;
import com.neu.prattle.dao.IUserDAO;
import com.neu.prattle.dao.UserDAOImpl;
import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.exceptions.UserDoesNotExistException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for AbstractDatabaseDAO class.
 */
public class DatabaseDAOTests {

    private IUserDAO userDAO;
    private IGroupDAO groupDAO;
    private Group group1;
    private Group group2;
    private User user1;
    private User user2;
    private User user3;
    private User user4;

    /**
     * Initial class set up.
     */
    @Before
    public void setUp() {
        userDAO = new UserDAOImpl();
        groupDAO = new GroupDAOImpl();
        user1 = new User("alecherry", "password", "Alessia",
                "Pizzoccheri", "Italian");
        user2 = new User("alexpho", "1234", "Alex",
                "Pho", "Japanese");
        user3 = new User("dsghdsh", "123", "Amit", "Madgi", "ENG");
        user4 = new User("sanjuv", "123", "Sanju", "V", "ENG");
        group1 = new Group("desperadoes", "a bunch of little rebels", user1);
        group2 = new Group("rebels", "some description goes here", user4);
    }

    /**
     * Test methods.
     */
    @Test
    public void testMethods() {
        assertNotNull(userDAO);
        assertNotNull(groupDAO);
        userDAO.addUser(user1);
        userDAO.addUser(user2);
        assertNotNull(userDAO.findUser("alexpho"));
        userDAO.removeUser("alexpho");
        userDAO.addUser(user3);
        Assert.assertEquals("dsghdsh", userDAO.findUser("dsghdsh").getUsername());
        // test groups
        groupDAO.addGroup(group1);
        userDAO.addUser(user2);
        groupDAO.addGroup(group1);
        groupDAO.addGroup(group2);
        assertNotNull(groupDAO.findGroup("desperadoes"));
        assertEquals("rebels", groupDAO.findGroup("rebels").getGroupName());
        userDAO.removeUser("alexpho");
        userDAO.removeUser(user3.getUsername());
        groupDAO.removeGroup(group1.getGroupName());
        groupDAO.removeGroup(group1.getGroupName());
        groupDAO.removeGroup(group2.getGroupName());
    }

    /**
     * Test insert method.
     */
    @Test
    public void testUpdate() {
        // update user
        userDAO.addUser(user3);
        assertEquals("123", userDAO.findUser("dsghdsh").getPassword());
        user3.setPassword("password");
        userDAO.updateUser(user3);
        assertEquals("password", userDAO.findUser("dsghdsh").getPassword());
        userDAO.removeUser("dsghdsh");
        // update group
        groupDAO.addGroup(group2);
        assertEquals("some description goes here", groupDAO.findGroup("rebels").getDescription());
        group2.setDescription("This is the new description");
        groupDAO.updateGroup(group2);
        assertEquals("This is the new description", groupDAO.findGroup("rebels").getDescription());
        groupDAO.removeGroup("rebels");
    }

    /**
     * Test UserAlreadyPresentException.
     */
    @Test(expected = UserAlreadyPresentException.class)
    public void testUserAlreadyPresentException() {
        userDAO.addUser(user1);
        userDAO.addUser(user1);
    }

    /**
     * Test UserDoesNotExistException.
     */
    @Test(expected = UserDoesNotExistException.class)
    public void testUserDoesNotExist() {
        userDAO.findUser("alessia123");
    }

    /**
     * Delete users from database.
     */
    @After
    public void cleanDatabase() {
        try {

            userDAO.removeUser(user1.getUsername());
            userDAO.removeUser(user2.getUsername());
            userDAO.removeUser(user3.getUsername());
            userDAO.removeUser(user4.getUsername());
            groupDAO.removeGroup(group1.getGroupName());
            groupDAO.removeGroup(group2.getGroupName());
        } catch (UserDoesNotExistException e) {
            // do nothing
        }
    }
}
