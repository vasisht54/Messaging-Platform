package com.neu.prattle.db;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * Test suite for MongoDB class.
 */
public class MongoDBTests {
    private DBConnection testConn;
    private Database groupDB;
    private Database userDB;
    private User user;
    private Group group;
    private String dbUrl;
    private String dbCollectionName;
    private String dbCollectionGroups;
    private String dbCollectionUsers;
    private String dbCollectionChats;
    private String dbCollectionMessages;

    /**
     * Initial set up.
     */
    @Before
    public void setup() {
        testConn = new DBConnection();
        groupDB = new MongoDBGroup();
        userDB = new MongoDBUser();
        user = new User("testUser", "password", "Alessia",
                "Pizzoccheri", "Italian");
        group = new Group("testGroup", "a bunch of little rebels", user);
        Properties properties = new Properties();
        try {
            properties.load(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("mongodb.properties")));
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        dbCollectionName = properties.getProperty("db.collection.name");
        dbCollectionChats = properties.getProperty("db.collection.chats");
        dbCollectionGroups = properties.getProperty("db.collection.groups");
        dbCollectionMessages = properties.getProperty("db.collection.messages");
        dbCollectionUsers = properties.getProperty("db.collection.users");
        dbUrl = properties.getProperty("db.url");
    }

    /**
     * Test user methods.
     */
    @Test
    public void testUserMethods() {
        assertNotNull(testConn);
        assertEquals("cs_5500", dbCollectionName);
        assertEquals("users", dbCollectionUsers);
        assertEquals("groups", dbCollectionGroups);
        assertEquals("message_logs", dbCollectionMessages);
        assertEquals("chat_logs", dbCollectionChats);
        assertEquals("mongodb+srv://madgiamit:amit123@wbdv-sp20-yf48p.mongodb.net/cs_5500?retryWrites=true&w=majority", dbUrl);
        // add user
        userDB.addUser(user);
        assertEquals("testUser", userDB.findUser("testUser").getUsername());
        assertNull(groupDB.findGroup("some group"));
        // remove user
        userDB.removeUser("testUser");
        assertNull(userDB.findUser("testUser"));
        // user does not exist
        assertNull(userDB.findUser("testuser"));
    }

    /**
     * Test group methods.
     */
    @Test
    public void testGroupMethods() {
        // add user
        groupDB.addGroup(group);
        assertEquals("testGroup", groupDB.findGroup("testGroup").getGroupName());
        // remove user
        groupDB.removeGroup("testGroup");
        assertNull(groupDB.findGroup("testGroup"));
        // group does not exist
        assertNull(groupDB.findGroup("some group"));
        assertNull(userDB.findUser("someuser"));
    }

    /**
     * Test insert method.
     */
    @Test
    public void testUpdate() {
        assertNotNull(userDB.findUsers());
        // update user
        User user2 = new User("sanjuv", "123", "Sanju", "V", "ENG");
        userDB.addUser(user2);
        assertEquals("Sanju", userDB.findUser("sanjuv").getFirstName());
        user2.setFirstName("Sanju Boy");
        userDB.updateUser(user2);
        assertEquals("Sanju Boy", userDB.findUser("sanjuv").getFirstName());
        userDB.removeUser("sanjuv");
        // update group
        groupDB.addGroup(group);
        assertEquals("testGroup", groupDB.findGroup("testGroup").getGroupName());
        group.setDescription("If you see this, it worked");
        groupDB.updateGroup(group);
        assertEquals("If you see this, it worked", groupDB.findGroup("testGroup").getDescription());
        groupDB.removeGroup("testGroup");
    }

    /**
     * Test UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testUser1() {
        userDB.addGroup(group);
    }

    /**
     * Test UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testUser2() {
        userDB.updateGroup(group);
    }

    /**
     * Test UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testUser3() {
        userDB.findGroup("testgroup");
    }

    /**
     * Test UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testUser4() {
        userDB.removeGroup("testgroup");
    }

    /**
     * Test UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void test1() {
        groupDB.addUser(user);
    }

    /**
     * Test UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void test2() {
        groupDB.findUser("test user");
    }

    /**
     * Test UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void test3() {
        groupDB.removeUser("testuser");
    }

    /**
     * Test UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void test4() {
        groupDB.updateUser(user);
    }

    /**
     * Test UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void test5() {
        groupDB.findUsers();
    }
}