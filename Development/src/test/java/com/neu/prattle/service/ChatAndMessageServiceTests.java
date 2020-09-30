package com.neu.prattle.service;

import com.neu.prattle.exceptions.ChatDoesNotExistException;
import com.neu.prattle.model.Message;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChatAndMessageServiceTests {
    private static String id = "123a2345287286";
    private ChatAndMessageService service;
    private Message message;

    /**
     * Initial set up.
     */
    @Before
    public void setUp() {
        service = ChatAndMessageServiceImpl.getInstance();
        message = new Message();
        message.setFrom("testuser1");
        message.setTo("testuser2");
        message.setContent("Hello, world");
        message.setTimeStamp();
    }

    /**
     * Test methods.
     */
    @Test
    public void testMethods() {
        assertNotNull(service);
        service.addChat("testuser1", "testuser2");
        service.addChat("testuser1", "testuser2");
        assertEquals("testuser1testuser2", service.findChat("testuser1testuser2"));
        service.addChat(id);
        service.addChat(id);
        String msg = service.addMessage("123a2345287286", message);
        assertNotEquals("", service.findMessage(msg));
        assertNotEquals("testuser1testuser2", service.findChat(id));
        Message message2 = new Message();
        message2.setFrom("testuser2");
        message2.setContent("I am here");
        message2.setTimeStamp();
        service.addMessage(id, message2);
        assertEquals(2, service.findMessages(id).size());
        service.deleteChat("testuser1testuser2");
        service.deleteChat("123a2345287286");
        assertTrue(service.findMessages(id).isEmpty());
    }

    /**
     * Test UnsupportedOperationException.
     */
    @Test(expected = ChatDoesNotExistException.class)
    public void testChatDoesNotExistException() {
        service.addMessage("idonotexist", message);
    }
}
