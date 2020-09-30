package com.neu.prattle.controller;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test suite for AppController class.
 */
public class AppControllerTest {
    private AppController controller;

    /**
     * Initial set up.
     */
    @Before
    public void setUp() {
        controller = new AppController();
    }

    /**
     * Test methods.
     */
    @Test
    public void testMethods() {
        assertNotNull(controller.getUsers());
    }

    @Test
    public void testGroups() {
        assertNotNull(controller.getGroups("alice"));
    }

}
