package com.neu.prattle.websocket;

import com.neu.prattle.model.Message;
import org.junit.Before;
import org.junit.Test;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.*;

/**
 * Test suite for MessageDecoder and Encoder.
 */
public class MessageDecoderTest {
    private StringBuilder str;
    private SimpleDateFormat dateFormatter;
    private MessageDecoder decoder;
    private MessageEncoder encoder;
    private Message message;

    @Before
    public void setUp() {
        str = new StringBuilder();
        dateFormatter = new SimpleDateFormat("MM dd, yyyy HH:mm");
        decoder = new MessageDecoder();
        encoder = new MessageEncoder();
        message = new Message();
        message.setType("user");
        message.setFrom("alessia");
        message.setTo("amit");
        message.setContent("This is a test!");
        message.setTimeStamp();
    }

    /**
     * Test MessageDecoder.
     *
     * @throws IOException
     */
    @Test
    public void testMessageDecoder() throws IOException {
        assertNotNull(decoder);
        String json =
                "{\"type\": \"user\",\"from\": \"alecherryy\",\"content\": \"Hello, world!\",\"to\": \"amit\",\"timeStamp\": \""
                        + message.getTimeStamp()
                        + "\"}";
        assertEquals("\n" +
                        "Type: user" +
                        "\n" +
                        "From: alecherryy" +
                        "\n" +
                        "To: amit" +
                        "\n" +
                        "Content: Hello, world!" +
                        "\n" +
                        "Sent: " +
                        message.getTimeStamp(),
                decoder.decode(json).toString());
        assertFalse(decoder.willDecode(null));
        assertTrue(decoder.willDecode("This is a test"));
    }

    /**
     * Test MessageEncoder.
     *
     * @throws IOException
     */
    @Test
    public void testMessageEncoder() throws IOException, EncodeException {
        assertNotNull(encoder);
        assertEquals(
                "{\"type\":\"user\",\"from\":\"alessia\",\"to\":\"amit\",\"content\":\"This is a test!\",\"timeStamp\":"
                        + "\""
                        + message.getTimeStamp()
                        + "\",\"thread\":[],\"status\":false}",
                encoder.encode(message));
    }
}