package com.neu.prattle.controller;

import com.google.gson.Gson;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.core.SynchronousDispatcher;
import org.jboss.resteasy.core.SynchronousExecutionContext;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import java.net.URISyntaxException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GroupControllerTests {

    private static Dispatcher dispatcher;
    private GroupController controller;
    private UserService userService;
    private Gson gson;
    private GroupController groupController;

    @Before
    public void setUp() {
        controller = new GroupController();
        userService = UserServiceImpl.getInstance();
        gson = new Gson();
        dispatcher = MockDispatcherFactory.createDispatcher();
        groupController = new GroupController();
        dispatcher.getRegistry().addSingletonResource(groupController);
    }

    @Test
    public void testCreateGroup() {
        User user = userService.findUser("janedoe");
        Group group = new Group("test-create-group", "Testing testing", user);
        Response actualResponse = controller.createGroup(group);
        assertEquals(Response.Status.OK.getStatusCode(), actualResponse.getStatus());
        controller.deleteGroup(group.getGroupName(), user);
    }

    @Test
    public void testCreateNullGroup() {
        Group group = null;
        Response actualResponse = controller.createGroup(group);
        assertEquals(409, actualResponse.getStatus());
    }

    @Test
    public void testDeleteGroup() {
        User user = userService.findUser("janedoe");
        Group group = new Group("test-delete-group", "Testing testing", user);
        controller.createGroup(group);
        Response actualResponse = controller.deleteGroup(group.getGroupName(), user);
        assertEquals(Response.Status.OK.getStatusCode(), actualResponse.getStatus());
    }


    @Test
    public void testDeleteGroup2() {
        User user = userService.findUser("janedoe");
        Group group = new Group("test-delete-group", "Testing testing", user);
        controller.createGroup(group);
        User member = userService.findUser("johnsnow");
        Response actualResponse = controller.deleteGroup(group.getGroupName(), member);
        assertEquals(409, actualResponse.getStatus());
    }

    @Test
    public void testDeleteNullGroup() {
        User user = new User("dummy", "dummy", "dummy", "dummy", "dummy");
        Response actualResponse = controller.deleteGroup("random", user);
        assertEquals(409, actualResponse.getStatus());
    }

    @Test
    public void testFindGroup() {

        User user = userService.findUser("janedoe");
        Group group = new Group("test-create-group", "Testing testing", user);
        Response actualResponse = controller.createGroup(group);
        assertEquals(Response.Status.OK.getStatusCode(), actualResponse.getStatus());

        Group returnedGroup = controller.findGroup("test-create-group");
        assertEquals("test-create-group", returnedGroup.getGroupName());
        assertEquals("Testing testing", returnedGroup.getDescription());
        assertEquals(user.getUsername(), returnedGroup.getListOfModerators().get(0).getUsername());
        assertEquals(user.getUsername(), returnedGroup.getListOfUsers().get(0).getUsername());

        controller.deleteGroup(returnedGroup.getGroupName(), user);
    }

    @Test
    public void testFindGroupWithNoName() {
        assertNull(controller.findGroup(""));
    }

    @Test
    public void testAddMembers() {
        User user = userService.findUser("janedoe");
        Group group = new Group("test-create-group", "Testing testing", user);
        Response actualResponse = controller.createGroup(group);
        assertEquals(Response.Status.OK.getStatusCode(), actualResponse.getStatus());

        Response response = controller.addMembers("test-create-group", "philmane", user);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Group returnedGroup = controller.findGroup("test-create-group");
        assertEquals(Arrays.asList(user, "philmane").size(), returnedGroup.getListOfUsers().size());

        controller.deleteGroup(returnedGroup.getGroupName(), user);
    }

    @Test
    public void testAddMembersArray() {
        User user = userService.findUser("janedoe");
        Group group = new Group("test-create-group", "Testing testing", user);
        controller.createGroup(group);
        Response response = controller.addMembers("test-create-group", "johnsnow", user);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Group returnedGroup = controller.findGroup("test-create-group");
        assertEquals(Arrays.asList(user, "johnsnow").size(), returnedGroup.getListOfUsers().size());

        controller.deleteGroup(returnedGroup.getGroupName(), user);
    }

    @Test
    public void testAddMembers2() {
        User user = userService.findUser("janedoe");
        Group group = new Group("test-create-group", "Testing testing", user);
        controller.createGroup(group);
        User member = userService.findUser("johnsnow");
        Response response = controller.addMembers("test-create-group", "philmane", member);
        assertEquals(409, response.getStatus());
        Group returnedGroup = controller.findGroup("test-create-group");
        controller.deleteGroup(returnedGroup.getGroupName(), user);
    }

    @Test
    public void testAddModerator() {

        User user = userService.findUser("janedoe");
        Group group = new Group("test-create-group", "Testing testing", user);
        controller.createGroup(group);
        Response actualResponse = controller.addModerator("test-create-group", "johnsnow", user);
        assertEquals(Response.Status.OK.getStatusCode(), actualResponse.getStatus());
        Group returnedGroup = controller.findGroup("test-create-group");
        assertEquals(2, returnedGroup.getListOfModerators().size());
        controller.deleteGroup(returnedGroup.getGroupName(), user);
    }

    @Test
    public void testRemoveMember() {

        User user = userService.findUser("janedoe");
        Group group = new Group("test-create-group", "Testing testing", user);
        controller.createGroup(group);
        controller.addMembers("test-create-group", "johnsnow", user);
        Group returnedGroup = controller.findGroup("test-create-group");
        assertEquals(2, returnedGroup.getListOfUsers().size());
        Response actualResponse = controller.removeMember("test-create-group", "johnsnow", user);
        assertEquals(Response.Status.OK.getStatusCode(), actualResponse.getStatus());
        returnedGroup = controller.findGroup("test-create-group");
        assertEquals(1, returnedGroup.getListOfUsers().size());
        controller.deleteGroup(returnedGroup.getGroupName(), user);
    }

    @Test
    public void testFindGroupMembers() {
        User user = userService.findUser("janedoe");
        Group group = new Group("test-create-group", "Testing testing", user);
        controller.createGroup(group);
        Response response = controller.addMembers("test-create-group", "johnsnow", user);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Response response1 = controller.addMembers("test-create-group", "philmane", user);
        assertEquals(Response.Status.OK.getStatusCode(), response1.getStatus());
        controller.deleteGroup(group.getGroupName(), user);
    }

    @Test
    public void testFindGroupModerators() {
        User user = userService.findUser("janedoe");
        Group group = new Group("test-create-group", "Testing testing", user);
        controller.createGroup(group);
        Group returnedGroup = controller.findGroup("test-create-group");
        assertEquals(returnedGroup.getListOfModerators(),
                controller.findGroupModerators("test-create-group").getEntity());
        controller.deleteGroup(group.getGroupName(), user);
    }

    @Test
    public void testFindGroupModerators2() {
        User user = userService.findUser("janedoe");
        Group group = new Group("test-create-group", "Testing testing", user);
        controller.createGroup(group);
        Response res = controller.findGroupModerators("killMonger");
        assertEquals(409, res.getStatus());
        controller.deleteGroup(group.getGroupName(), user);
    }

    @Test
    public void testRemoveModerator() {
        User user = userService.findUser("janedoe");
        Group group = new Group("test-create-group", "Testing testing", user);
        controller.createGroup(group);
        controller.addModerator("test-create-group", "johnsnow", user);
        Group returnedGroup = controller.findGroup("test-create-group");
        assertEquals(2, returnedGroup.getListOfModerators().size());
        Response actualResponse = controller.removeModerator("test-create-group", "johnsnow", user);
        assertEquals(Response.Status.OK.getStatusCode(), actualResponse.getStatus());
        returnedGroup = controller.findGroup("test-create-group");
        assertEquals(1, returnedGroup.getListOfModerators().size());
        controller.deleteGroup(group.getGroupName(), user);
    }

    @Test
    public void testGroupCreateExistMemModerator() {
        User user = userService.findUser("janedoe");
        Group group = new Group("test-create-group", "Testing testing", user);
        controller.createGroup(group);
        Response response = controller.addMembers("test-create-group", "philmane", user);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Response actualResponse = controller.addModerator("test-create-group", "philmane", user);
        assertEquals(Response.Status.OK.getStatusCode(), actualResponse.getStatus());
        Group returnedGroup = controller.findGroup("test-create-group");
        assertEquals(2, returnedGroup.getListOfModerators().size());
        controller.deleteGroup(returnedGroup.getGroupName(), user);
    }

    @Test
    public void testGroupExistingModerator() {
        User user = userService.findUser("janedoe");
        Group group = new Group("test-create-group", "Testing testing", user);
        controller.createGroup(group);
        Response response = controller.addMembers("test-create-group", "philmane", user);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Response actualResponse = controller.addModerator("test-create-group", "janedoe", user);
        assertEquals(409, actualResponse.getStatus());
        Group returnedGroup = controller.findGroup("test-create-group");
        assertEquals(1, returnedGroup.getListOfModerators().size());
        controller.deleteGroup(returnedGroup.getGroupName(), user);
    }

    @Test
    public void testGroupAddNoModerator() {
        User user = userService.findUser("janedoe");
        Group group = new Group("test-create-group", "Testing testing", user);
        controller.createGroup(group);
        Response response = controller.addMembers("test-create-group", "philmane", user);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        User member = userService.findUser("philmane");
        Response actualResponse = controller.addModerator("test-create-group", "janedoe", member);
        assertEquals(409, actualResponse.getStatus());
        Group returnedGroup = controller.findGroup("test-create-group");
        assertEquals(1, returnedGroup.getListOfModerators().size());
        controller.deleteGroup(returnedGroup.getGroupName(), user);
    }

    @Test
    public void testGroupDoesNotExist() {
        User user = userService.findUser("janedoe");
        Response response = controller.addMembers("notFound", "philmane", user);
        assertEquals(409, response.getStatus());
    }

    @Test
    public void testGroupDoesNotExistModerator() {
        User user = userService.findUser("janedoe");
        Response response = controller.addModerator("notFound", "philmane", user);
        assertEquals(409, response.getStatus());
    }

    @Test
    public void testGroupDoesNotExistRemoveMember() {
        User user = userService.findUser("janedoe");
        Response response = controller.removeMember("notFound", "philmane", user);
        assertEquals(409, response.getStatus());
    }

    @Test
    public void testGroupDoesNotExistRemoveModerator() {
        User user = userService.findUser("janedoe");
        Response response = controller.removeModerator("notFound", "philmane", user);
        assertEquals(409, response.getStatus());
    }

    @Test
    public void testRemoveMemberDOesNotExist() {

        User user = userService.findUser("janedoe");
        Group group = new Group("test-create-group", "Testing testing", user);
        controller.createGroup(group);
        controller.addMembers("test-create-group", "johnsnow", user);
        Group returnedGroup = controller.findGroup("test-create-group");
        assertEquals(2, returnedGroup.getListOfUsers().size());
        Response actualResponse = controller.removeMember("test-create-group", "philmane", user);
        assertEquals(409, actualResponse.getStatus());
        returnedGroup = controller.findGroup("test-create-group");
        assertEquals(2, returnedGroup.getListOfUsers().size());
        controller.deleteGroup(returnedGroup.getGroupName(), user);
    }

    @Test
    public void testRemoveMemberDoesNotExist2() {

        User user = userService.findUser("janedoe");
        Group group = new Group("test-create-group", "Testing testing", user);
        controller.createGroup(group);
        controller.addMembers("test-create-group", "johnsnow", user);
        Group returnedGroup = controller.findGroup("test-create-group");
        assertEquals(2, returnedGroup.getListOfUsers().size());
        User member = userService.findUser("johnsnow");
        Response actualResponse = controller.removeMember("test-create-group", "janedoe", member);
        assertEquals(409, actualResponse.getStatus());
        returnedGroup = controller.findGroup("test-create-group");
        assertEquals(2, returnedGroup.getListOfUsers().size());
        controller.deleteGroup(returnedGroup.getGroupName(), user);
    }


    @Test
    public void testRemoveModeratorDOesNotExist() {

        User user = userService.findUser("janedoe");
        Group group = new Group("test-create-group", "Testing testing", user);
        controller.createGroup(group);
        controller.addMembers("test-create-group", "johnsnow", user);
        Group returnedGroup = controller.findGroup("test-create-group");
        assertEquals(2, returnedGroup.getListOfUsers().size());
        Response actualResponse = controller.removeModerator("test-create-group", "philmane", user);
        assertEquals(409, actualResponse.getStatus());
        returnedGroup = controller.findGroup("test-create-group");
        assertEquals(2, returnedGroup.getListOfUsers().size());
        controller.deleteGroup(returnedGroup.getGroupName(), user);
    }

    @Test
    public void testRemoveModeratorDoesNotExist2() {

        User user = userService.findUser("janedoe");
        Group group = new Group("test-create-group", "Testing testing", user);
        controller.createGroup(group);
        controller.addMembers("test-create-group", "johnsnow", user);
        Group returnedGroup = controller.findGroup("test-create-group");
        assertEquals(2, returnedGroup.getListOfUsers().size());
        User member = userService.findUser("johnsnow");
        Response actualResponse = controller.removeModerator("test-create-group", "janedoe", member);
        assertEquals(409, actualResponse.getStatus());
        returnedGroup = controller.findGroup("test-create-group");
        assertEquals(2, returnedGroup.getListOfUsers().size());
        controller.deleteGroup(returnedGroup.getGroupName(), user);
    }

    @Test
    public void testGetGroupMembers() {
        User user = userService.findUser("janedoe");
        Group group = new Group("test-create-group", "Testing testing", user);
        controller.createGroup(group);
        Response response = controller.addMembers("test-create-group", "johnsnow", user);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Response response1 = controller.addMembers("test-create-group", "philmane", user);
        assertEquals(Response.Status.OK.getStatusCode(), response1.getStatus());
        Group returnedGroup = controller.findGroup("test-create-group");
        assertEquals(returnedGroup.getListOfUsers(),
                controller.findGroupMembers("test-create-group").getEntity());
        controller.deleteGroup(group.getGroupName(), user);
    }

    @Test
    public void testGetGroupMembers2() {
        User user = userService.findUser("janedoe");
        Group group = new Group("test-create-group", "Testing testing", user);
        controller.createGroup(group);
        Response response = controller.addMembers("test-create-group", "johnsnow", user);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Response response1 = controller.addMembers("test-create-group", "philmane", user);
        assertEquals(Response.Status.OK.getStatusCode(), response1.getStatus());
        Response response2 = controller.findGroupMembers("Drogon");
        assertEquals(409, response2.getStatus());
        controller.deleteGroup(group.getGroupName(), user);
    }


    @Test
    public void testMergeGroups() {
        User user = userService.findUser("janedoe");
        Group group = new Group("test-create-group", "Testing testing", user);
        controller.createGroup(group);
        Response response = controller.addMembers("test-create-group", "johnsnow", user);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Response response1 = controller.addMembers("test-create-group", "philmane", user);
        assertEquals(Response.Status.OK.getStatusCode(), response1.getStatus());
        Group groupA = controller.findGroup("test-create-group");

        User user2 = userService.findUser("alice");
        group = new Group("test-merge-group", "Testing testing", user2);
        controller.createGroup(group);
        response = controller.addMembers("test-merge-group", "bvasquez", user2);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        response1 = controller.addMembers("test-merge-group", "amadgi", user2);
        assertEquals(Response.Status.OK.getStatusCode(), response1.getStatus());
        Group groupB = controller.findGroup("test-merge-group");

        Response mergeResponse = controller.mergeGroups(groupA.getGroupName(), groupB.getGroupName(), user);
        assertEquals(Response.Status.OK.getStatusCode(), mergeResponse.getStatus());
        Group mergedGroupA = controller.findGroup(groupA.getGroupName());
        assertEquals(6, mergedGroupA.getListOfUsers().size());

        controller.deleteGroup(groupA.getGroupName(), user);
        controller.deleteGroup(groupB.getGroupName(), user2);
    }

    @Test
    public void testInvalidGroupMerging() {
        User user = userService.findUser("janedoe");
        Group group = new Group("test-create-group", "Testing testing", user);
        controller.createGroup(group);
        Response response = controller.addMembers("test-create-group", "johnsnow", user);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Response response1 = controller.addMembers("test-create-group", "philmane", user);
        assertEquals(Response.Status.OK.getStatusCode(), response1.getStatus());
        Group groupA = controller.findGroup("test-create-group");

        Response mergeResponse = controller.mergeGroups(groupA.getGroupName(), null, user);
        assertEquals(409, mergeResponse.getStatus());

        controller.deleteGroup(groupA.getGroupName(), user);
    }

    private MockHttpResponse sendHttpRequest(MockHttpRequest request, MockHttpResponse response) {
        dispatcher.invoke(request, response);
        return response;
    }

    /**
     * Test POST request.
     *
     * @throws URISyntaxException
     * @throws JAXBException
     */
    private MockHttpResponse sendAsyncPostRequest(String path, String requestBody)
            throws URISyntaxException,
            JAXBException {
        MockHttpRequest request = MockHttpRequest.post(path);
        request.accept(MediaType.APPLICATION_JSON_TYPE);
        request.contentType(MediaType.APPLICATION_JSON_TYPE);
        request.content(requestBody.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        SynchronousExecutionContext synchronousExecutionContext = new SynchronousExecutionContext(
                (SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(synchronousExecutionContext);
        return sendHttpRequest(request, response);
    }

    @Test
    public void testPasswordProtectedGroups() throws JAXBException, URISyntaxException {
        User user = userService.findUser("janedoe");
        Group group = new Group("protectedGroup", "A test for password protected group", user, "groupPassword");
        controller.createGroup(group);

        GroupLogin loginGroup = new GroupLogin();
        loginGroup.setGroupName("protectedGroup");
        loginGroup.setPassword("groupPassword");
        MockHttpResponse httpResponse = sendAsyncPostRequest("/group/authenticate", gson.toJson(loginGroup));
        Assert.assertEquals(200, httpResponse.getStatus());

        controller.deleteGroup("protectedGroup", user);
    }

    @Test
    public void testIncorrectPassword() throws JAXBException, URISyntaxException {
        User user = userService.findUser("janedoe");
        Group group = new Group("protectedGroup", "A test for password protected group", user, "groupPassword");
        controller.createGroup(group);

        GroupLogin loginGroup = new GroupLogin();
        loginGroup.setGroupName("protectedGroup");
        loginGroup.setPassword("wrong");
        MockHttpResponse httpResponse = sendAsyncPostRequest("/group/authenticate", gson.toJson(loginGroup));
        Assert.assertEquals(401, httpResponse.getStatus());

        controller.deleteGroup("protectedGroup", user);
    }

    @Test
    public void testGroupDoesntExist() throws JAXBException, URISyntaxException {
        User user = userService.findUser("janedoe");
        Group group = new Group("protectedGroup", "A test for password protected group", user, "groupPassword");
        controller.createGroup(group);

        GroupLogin loginGroup = new GroupLogin();
        loginGroup.setGroupName("doesntExist");
        loginGroup.setPassword("wrong");
        MockHttpResponse httpResponse = sendAsyncPostRequest("/group/authenticate", gson.toJson(loginGroup));
        Assert.assertEquals(401, httpResponse.getStatus());

        controller.deleteGroup("protectedGroup", user);
    }


}
