package com.neu.prattle.controller;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test suite for the login class.
 */
public class LoginTests {
    private Login test;

    /**
     * Initial set yup.
     */
    @Before
    public void setUp() {
        test = new Login();
    }

    /**
     * Test getter methods.
     */
    @Test
    public void testMethods() {
        assertNotNull(test);
        assertNull(test.getUsername());
        assertNull(test.getPassword());
    }
}