package com.neu.prattle.controller;

import com.neu.prattle.model.Message;
import com.neu.prattle.service.ChatAndMessageService;
import com.neu.prattle.service.ChatAndMessageServiceImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the MessageController class.
 */
public class MessageControllerTests {
    private static String id = "0ef00bacb0202c25629b587a";
    private MessageController controller;
    private ChatAndMessageService service;
    private Message message;
    private Login obj;

    /**
     * Initial set up.
     */
    @Before
    public void setUp() {
        controller = new MessageController();
        obj = new Login();
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
        assertNotNull(obj);
        assertNull(obj.getPassword());
        assertNull(obj.getUsername());
        service.addChat("testuser1testuser2");
        service.addChat(id);
        String msg = service.addMessage(id, message);
        SingleString test = new SingleString();
        test.setValue(msg);
        assertEquals(200, controller.addThread(test).getStatus());
        assertEquals(200, controller.addMessageToThread(test.getValue(), message).getStatus());
        assertEquals(1, controller.findThread(msg).size());
        SingleString str = new SingleString();
        str.setValue(msg);
        assertEquals(200, controller.addThread(str).getStatus());
        str.setValue("0ef00bacb0202c25629b587f");
        assertEquals("0ef00bacb0202c25629b587f", str.getValue());
        assertEquals(200, controller.deleteMessage(str).getStatus());
        assertTrue(controller.findMessageHistory("idonotexist").isEmpty());
        assertEquals(1, controller.findMessageHistory(id).size());
        service.deleteChat(id);
        service.deleteChat("testuser1testuser2");
        service.deleteChat(str.getValue());
        service.deleteChat(str.getValue());
    }
}
