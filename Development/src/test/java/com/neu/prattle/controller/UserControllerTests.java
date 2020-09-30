package com.neu.prattle.controller;

import com.google.gson.Gson;
import com.neu.prattle.db.Database;
import com.neu.prattle.db.MongoDBUser;
import com.neu.prattle.exceptions.UserDoesNotExistException;
import com.neu.prattle.model.User;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.core.SynchronousDispatcher;
import org.jboss.resteasy.core.SynchronousExecutionContext;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.junit.*;
import org.mockito.Mock;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.mockito.Mockito.mock;

/**
 * Test suite for UserController class.
 */
public class UserControllerTests {

    private static Dispatcher dispatcher;
    private static UserController userController;
    private static User testUser1;
    private static User testUser2;
    private static User testUser3;
    private static User testUser4;
    private static User testUser5;
    private static String SIGNUP_ENDPOINT;
    private static String SIGNIN_ENDPOINT;
    private UserController controller;
    private Gson gson;
    private Database userDB;

    /**
     * Mock websocket sessions.
     */
    @Mock
    private Session session1;
    @Mock
    private Session session2;

    /**
     * Set up objects once.
     */
    @BeforeClass
    public static void setUpBefore() {
        userController = new UserController();
        testUser1 = new User();
        testUser2 = new User("rebelTwo", "test123", "Ale", "Ph", "English");
        testUser3 = new User("rebelThree", "test123", "BLM", "Reform", "English");
        testUser4 = new User("janedoe", "password", "Jane", "Doe", "english");
        testUser5 = new User("scoobyDoo", "scoobySnax", "Scooby", "Doo", "English");
        SIGNUP_ENDPOINT = "/user/signup";
        SIGNIN_ENDPOINT = "/user/signin";
    }

    /**
     * Test object set up.
     */
    @Before
    public void setUp() throws IOException, EncodeException {
        controller = new UserController();
        session1 = mock(Session.class);
        session2 = mock(Session.class);
        dispatcher = MockDispatcherFactory.createDispatcher();
        userController = new UserController();
        dispatcher.getRegistry().addSingletonResource(userController);
        gson = new Gson();
        userDB = new MongoDBUser();
    }

    /**
     * Test for creating a new user.
     */
    @Test
    public void testWebSocketCreateUserSetter() throws Exception {
        testUser1.setFirstName("John");
        testUser1.setLastName("Smith");
        testUser1.setPrimaryLanguage("English");
        testUser1.setUsername("jsmith");
        testUser1.setPassword("test123");
        MockHttpResponse httpResponse = sendAsyncPostRequest(SIGNUP_ENDPOINT, gson.toJson(testUser1));
        Assert.assertEquals(Response.Status.OK.getStatusCode(), httpResponse.getStatus());
        MockHttpResponse httpResponse2 = sendAsyncPostRequest(SIGNUP_ENDPOINT, gson.toJson(testUser2));
        Assert.assertEquals(Response.Status.OK.getStatusCode(), httpResponse2.getStatus());
    }

    /**
     * Test for user that already exists.
     *
     * @throws Exception if user already exists
     */
    @Test
    public void testWebSocketCreateUserAlreadyExists() throws URISyntaxException, JAXBException {
        User mockUser = new User("rebelFour", "test123", "BLM", "Reform", "English");
        sendAsyncPostRequest(SIGNUP_ENDPOINT, gson.toJson(mockUser));
        MockHttpResponse responseTwo = sendAsyncPostRequest(SIGNUP_ENDPOINT, gson.toJson(mockUser));
        Assert.assertEquals(409, responseTwo.getStatus());
    }

    /**
     * Test for sign in as existing user.
     */
    @Test
    public void testWebSocketSignInUser() throws URISyntaxException, JAXBException {
        Login loginUser = new Login();
        loginUser.setUsername("scoobyDoo");
        loginUser.setPassword("scoobySnax");
        controller.createUserAccount(testUser5);
        MockHttpResponse httpResponse = sendAsyncPostRequest(SIGNIN_ENDPOINT, gson.toJson(loginUser));
        Assert.assertEquals(200, httpResponse.getStatus());
    }

    @Test
    public void testSignInUserDoesNotExist() throws URISyntaxException, JAXBException {
        Login loginUser = new Login();
        MockHttpResponse httpResponse = sendAsyncPostRequest(SIGNIN_ENDPOINT, gson.toJson(loginUser));
        Assert.assertEquals(401, httpResponse.getStatus());
        Assert.assertNull(loginUser.getUsername());
        Assert.assertNull(loginUser.getPassword());
    }

    @Test
    public void testSignInUserExistsBadPass() throws URISyntaxException, JAXBException {
        Login loginUser = new Login();
        loginUser.setUsername("rebelTwo");
        loginUser.setPassword("fagioli");
        controller.createUserAccount(testUser2);
        MockHttpResponse httpResponse = sendAsyncPostRequest(SIGNIN_ENDPOINT, gson.toJson(loginUser));
        Assert.assertEquals(401, httpResponse.getStatus());
    }

    @Test
    public void testSignInUserExistsBadUsername() throws URISyntaxException, JAXBException {
        Login loginUser = new Login();
        loginUser.setUsername("sonoPicasso");
        loginUser.setPassword("minestrone");
        User mockUser = new User("sonoPicassoS", "minestrone", "Pablo", "Picasso", "Spanish");
        controller.createUserAccount(mockUser);
        Assert.assertNotEquals(loginUser.getUsername(), mockUser.getUsername());
        MockHttpResponse httpResponse = sendAsyncPostRequest(SIGNIN_ENDPOINT, gson.toJson(loginUser));
        Assert.assertEquals(401, httpResponse.getStatus());
    }

    @Test
    public void testFindUser() throws IOException {
        User user = new User("testUser", "User", "user", "user", "english");
        controller.createUserAccount(user);
        Assert.assertEquals(
                "{\"username\":\"testUser\",\"password\":\"User\",\"firstName\":\"user\",\"lastName\":\"user\",\"primaryLanguage\":\"english\",\"listOfGroups\":[]}",
                controller.findUser(user.getUsername()));
        controller.deleteUser(user.getUsername());
    }

    /**
     * Test UserAlreadyPresentException.
     */
    @Test(expected = UserDoesNotExistException.class)
    public void testUserDoesNotExistException() throws IOException {
        controller.findUser("");
    }

    /**
     * Test deleteUser
     */
    @Test
    public void testDeleteUserValid() {
        User user = new User("testUser", "User", "user", "user", "english");
        controller.createUserAccount(user);
        Response response = controller.deleteUser("testUser");
        Assert.assertEquals(200, response.getStatus());
    }

    /**
     * Test deleteUser that doesn't exist
     */
    @Test
    public void testDeleteUserThatDoesntExist() {
        Response response = controller.deleteUser("slghsg");
        Assert.assertEquals(409, response.getStatus());
    }

    @Test
    public void testDeleteUserThatDoesntExistWS() throws URISyntaxException, JAXBException {
        Login loginUser = new Login();
        MockHttpResponse httpResponse = sendAsyncPostRequest("/delete/slghsg", gson.toJson(loginUser));
        Assert.assertEquals(404, httpResponse.getStatus());
    }

    @Test
    public void testDeleteUserEmptyString() {
        Response response = controller.deleteUser("");
        Assert.assertEquals(409, response.getStatus());
    }

    /**
     * Test POST request.
     *
     * @throws URISyntaxException
     */
    private MockHttpResponse sendAsyncPostRequest(String path, String requestBody)
            throws URISyntaxException {
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

    private MockHttpResponse sendHttpRequest(MockHttpRequest request, MockHttpResponse response) {
        dispatcher.invoke(request, response);
        return response;
    }

    /**
     * Tests for objects methods.
     */
    @Test
    public void testMethods() {
        Assert.assertEquals(200, controller.createUserAccount(testUser3).getStatus());
        User user4 = new User("rebelThree", "1234567", "Alex", "Rebel", "Latin");
        Assert.assertEquals(409, controller.createUserAccount(user4).getStatus());

    }

    /**
     * Test UserAlreadyPresentException.
     */
    @Test(expected = NullPointerException.class)
    public void testNullUser() {
        controller.createUserAccount(null).getStatus();
    }

    /**
     * Delete all test users.
     */
    @After
    public void deleteAll() {
        userDB.removeUser("amit");
        userDB.removeUser("alex");
        userDB.removeUser("dipen");
        userDB.removeUser("alessia");
        userDB.removeUser("sanju");
        userDB.removeUser("rebelThree");
        userDB.removeUser("rebelTwo");
        userDB.removeUser("jsmith");
        userDB.removeUser("rebelFour");
        userDB.removeUser("scoobyDoo");
        userDB.removeUser("sonoPicassoS");
    }
}
