package com.neu.prattle.dao;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for DatabaseDAOChat class.
 */
public class DatabaseDAOChatTests {
    private static String id = "5ef17ff0ca075967e4d881e9";
    private static String id2 = "5ef17ff0ca075967e4d881e4";
    private static String id3 = "5ef17ff0ca075967e4d881e5";
    private DatabaseDAOChat logDAO;
    private Document message;

    /**
     * Initial set up.
     */
    @Before
    public void setUp() {
        logDAO = new DatabaseDAOChat();
        message = new Document();
        message.put("_id", new ObjectId(id2));
        message.put("chat_id", id);
        message.put("from", "testuser1");
        message.put("content", "Hello, world");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        String date = dateFormatter.format(new Date());
        message.put("timeStamp", date);
    }

    /**
     * Test methods.
     */
    @Test
    public void testMethods() {
        assertNotNull(logDAO);
        assertEquals(id2, logDAO.createMessage(message));
        assertNotNull(logDAO.readMessages(id));
        Iterator iterator = logDAO.readMessages(id).iterator();
        List<String> content = new ArrayList<>();
        while (iterator.hasNext()) {
            Document doc = (Document) iterator.next();
            content.add((String) doc.get("content"));
        }
        assertEquals("Hello, world", content.get(0));
        Document message2 = new Document();
        message2.put("_id", new ObjectId(id3));
        message2.put("chat_id", id);
        message2.put("from", "testuser1");
        message2.put("content", "Hello, testuser1");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        String date = dateFormatter.format(new Date());
        message2.put("timeStamp", date);
        logDAO.createMessage(message2);
        logDAO.addThread(id3);
        assertEquals("[]", logDAO.readMessage(id3).get("thread").toString());
        logDAO.addMessageToThread(id3, message);
        List<Document> thread = (List<Document>) logDAO.readMessage(id3).get("thread");
        assertEquals(1, thread.size());
        logDAO.deleteMessage(id3);
        assertNull(logDAO.readMessage(id3));
        iterator = logDAO.readMessages(id).iterator();
        content = new ArrayList<>();
        while (iterator.hasNext()) {
            Document doc = (Document) iterator.next();
            content.add((String) doc.get("content"));
        }
        assertEquals("Hello, world", content.get(0));
        Document doc = new Document();
        doc.put("participants", "hellothere");
        logDAO.createChat(doc);
        logDAO.deleteMessages(id);
        assertFalse(logDAO.readMessages(id).iterator().hasNext());
        logDAO.deleteChat("hellothere");
        assertEquals("", logDAO.readChat("hellothere"));
    }
}
