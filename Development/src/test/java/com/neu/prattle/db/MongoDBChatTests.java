package com.neu.prattle.db;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for MongoDBChat class.
 */
public class MongoDBChatTests {
    private static String id = "5ee82e36872f6c2278a0ec81";
    private static String id2 = "5ee82e36872f6c2278a0ec82";
    private DatabaseChat log;
    private Document doc1;
    private Document doc2;

    /**
     * Initial set up.
     */
    @Before
    public void setUp() {
        log = new MongoDBChat();
        doc1 = new Document();
        doc2 = new Document();
        doc1.put("_id", id);
        doc1.put("participants", "alessiapalexpho");
    }

    /**
     * Test chat methods.
     */
    @Test
    public void testChatDB() {
        log.addChat(doc1);
        assertNotNull(log.findChat("alessiapalexpho"));
        assertEquals("alessiapalexpho", log.findChat("alessiapalexpho").get("participants"));
        log.deleteChat("alessiapalexpho");
        assertNull(log.findChat("alessiapalexpho"));
    }

    /**
     * Test message methods.
     */
    @Test
    public void testMessageDB() {
        String chat_id = "testingchat";
        doc1.remove("participants");
        doc1.put("_id", new ObjectId(id));
        doc1.put("chat_id", chat_id);
        doc1.put("from", "alessia");
        doc1.put("content", "This is a test");
        doc1.put("timeStamp", "06-12-2020");
        assertEquals(id, log.addMessage(doc1));
        log.addThread(id);
        assertNotNull(log.findMessages(id));
        doc2.put("_id", new ObjectId(id2));
        doc2.put("chat_id", chat_id);
        doc2.put("from", "alexpho");
        doc2.put("content", "This is a test response");
        doc2.put("timeStamp", "06-12-2020");
        log.addMessageToThread(id, doc2);
        List<Document> thread = (List<Document>) log.findMessage(id).get("thread");
        assertEquals(1, thread.size());
        doc1.put("_id", new ObjectId("5ee82e36872f6c2278a0ec84"));
        log.addMessageToThread(id, doc1);
        thread = (List<Document>) log.findMessage(id).get("thread");
        assertEquals(2, thread.size());
        log.addMessage(doc2);
        assertNotNull(log.findMessages(id2));
        assertNotNull(log.findMessages(chat_id));
        log.deleteMessage(id2);
        assertNull(log.findMessage(id2));
        Iterator iterator = log.findMessages(chat_id).iterator();
        List<String> content = new ArrayList<>();
        while (iterator.hasNext()) {
            Document doc = (Document) iterator.next();
            content.add((String) doc.get("content"));
        }
        assertEquals("This is a test", content.get(0));
        log.deleteMessages(chat_id);
        assertFalse(log.findMessages(chat_id).iterator().hasNext());
    }
}
