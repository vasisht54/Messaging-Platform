package com.neu.prattle.model;

import com.neu.prattle.exceptions.UserDoesNotExistException;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.*;

/**
 * Test suite for the Group class.
 */
public class GroupTests {
    private Group group1;
    private Group group2;
    private Group groupWPass;
    private User user;
    private User user2;
    private User user3;
    private User user4;
    private User user5;
    private List<User> sampleList;

    /**
     * Initial set up.
     */
    @Before
    public void setUp() {
        user = new User("alecherry", "password", "Alessia",
                "Pizzoccheri", "Italian");
        user2 = new User("alexpho", "1234", "Alex",
                "Pho", "Japanese");
        user3 = new User("amadgi", "123", "Amit", "Madgi", "ENG");
        user4 = new User("sanjuv", "123", "Sanju", "V", "ENG");
        user5 = new User("cheeseBall", "linguineLover42", "Leonardo", "Picasso", "Italian");
        group1 = new Group("desperadoes", "a bunch of little rebels", user);
        group2 = new Group();
        groupWPass = new Group("forza formaggio", "lots of cheese", user5);
        sampleList = new ArrayList<>();
    }

    /**
     * Test methods and class constructors.
     */
    @Test
    public void testMethods() {
        assertNotNull(group1);
        assertNotNull(group2);
        assertEquals("desperadoes", group1.getGroupName());
        group2.setGroupName("frivoli ragazzi");
        group2.setGroupUsername("thisisagroup");
        assertEquals("thisisagroup", group2.getGroupUsername());
        assertEquals("frivoli ragazzi", group2.getGroupName());
        assertEquals("a bunch of little rebels", group1.getDescription());
        assertNull(group2.getDescription());
        group2.setDescription("Hello, world!");
        assertEquals("Hello, world!", group2.getDescription());
        assertEquals(1, group1.getListOfModerators().size());
        assertEquals(1, group1.getListOfUsers().size());
        group1.addUsers(user2);
        List<User> testList = new ArrayList<>();
        testList.add(user3);
        testList.add(user4);
        group1.addUsers(testList);
        assertEquals(4, group1.getListOfUsers().size());
        // list all user
        Set<User> users = new HashSet<>(group1.getListOfUsers());
        StringBuilder str = new StringBuilder();
        for (User u : users) {
            str.append(u.getUsername() + " ");
        }

        assertEquals("amadgi alexpho alecherry sanjuv ", str.toString());
        group1.removeUser(user4);
        assertEquals(3, group1.getListOfUsers().size());
        group2.addUsers(user4);
        Group group3 = group1;
        group1.addGroup(group3);
        assertEquals(6, group1.getListOfUsers().size());
        // list all user
        users = new HashSet<>(group1.getListOfUsers());
        str = new StringBuilder();

        for (User u : users) {
            str.append(u.getUsername() + " ");
        }
        assertEquals("amadgi alexpho alecherry ", str.toString());
    }

    /**
     * Test for group creator.
     */
    @Test
    public void testGroupHasGroupCreator() {
        assertThat(groupWPass.getListOfUsers(), not(IsEmptyCollection.empty()));
        assertThat(groupWPass.getListOfUsers(), hasItem(user5));
    }

    /**
     * Test password.
     */
    @Test
    public void testGroupHasPasswordValidInput() {
        groupWPass.addUsers(user);
        assertThat(groupWPass.getListOfUsers(), hasItem(user));
    }

    @Test
    public void testAddAllUsersFromGroupHasPasswordValid() {
        group1.addUsers(user2);
        group1.addUsers(user3);
        group1.addUsers(user4);
        groupWPass.addGroup(group1);
        group1.addUsers(user5);

        assertThat(groupWPass.getListOfUsers(), containsInAnyOrder(
                hasProperty("username", is("alecherry")),
                hasProperty("username", is("alexpho")),
                hasProperty("username", is("sanjuv")),
                hasProperty("username", is("cheeseBall")),
                hasProperty("username", is("amadgi"))));
    }

    @Test
    public void testAddAllUsersFromGroupHasNoPasswordValid() {
        groupWPass.addUsers(user2);
        groupWPass.addUsers(user3);
        group1.addUsers(user4);
        groupWPass.addGroup(group1);

        assertThat(groupWPass.getListOfUsers(), containsInAnyOrder(
                hasProperty("username", is("alecherry")),
                hasProperty("username", is("alexpho")),
                hasProperty("username", is("sanjuv")),
                hasProperty("username", is("cheeseBall")),
                hasProperty("username", is("amadgi"))));
    }

    @Test
    public void testAddMultipleUsersHasPasswordValid() {
        sampleList.add(user);
        sampleList.add(user2);
        sampleList.add(user3);
        group2.addUsers(sampleList);
        assertThat(group2.getListOfUsers(), containsInAnyOrder(
                hasProperty("username", is("alecherry")),
                hasProperty("username", is("alexpho")),
                hasProperty("username", is("amadgi"))));
    }

    @Test
    public void testAddMultipleUsersHasNoPasswordValid() {
        sampleList.add(user);
        sampleList.add(user2);
        sampleList.add(user3);
        group2.addUsers(sampleList);

        assertThat(group2.getListOfUsers(), containsInAnyOrder(
                hasProperty("username", is("alecherry")),
                hasProperty("username", is("alexpho")),
                hasProperty("username", is("amadgi"))));
    }

    @Test
    public void testRemoveAMod() {
        group1.addUsers(user5);
        group1.removeModerator(user);
        assertThat(group1.getListOfModerators(), not(hasItem(user)));
    }

    /**
     * Test removing moderators.
     */
    @Test
    public void testRemoveAllMods() {
        group1.removeModerator(user);
        assertThat(group1.getListOfModerators(), not(hasItem(user)));
        group1.addUsers(user3);
        group1.addUsers(user2);
        group1.addModerator(user3);
        group1.addModerator(user2);
        group1.removeModerator(user2);
        group1.removeModerator(user3);
        assertThat(group1.getListOfModerators(), IsEmptyCollection.empty());
        assertEquals(0, group1.getListOfModerators().size());
    }

    /**
     * Remove a moderator/user that does not exist.
     */
    @Test(expected = UserDoesNotExistException.class)
    public void testRemoveModInvalid() {
        group2.removeModerator(user5);
        group1.removeUser(user2);
    }

    @Test
    public void testRemoveUserExistsNotMod() {
        group1.addUsers(user5);
        group1.addUsers(user3);
        group1.removeUser(user5);
        assertThat(group1.getListOfUsers(), not(hasItem(user5)));
        assertEquals(2, group1.getListOfUsers().size());
    }

    @Test
    public void testRemoveUserExistsIsMod() {
        try {
            group1.addUsers(user5);
            group1.addUsers(user3);
            group1.removeUser(user);
            assertThat(group1.getListOfUsers(), not(hasItem(user)));
            assertThat(group1.getListOfModerators(), not(hasItem(user)));
            assertEquals(2, group1.getListOfUsers().size());
            assertEquals(0, group1.getListOfModerators().size());
        } catch (UserDoesNotExistException e) {

        }
    }
}
