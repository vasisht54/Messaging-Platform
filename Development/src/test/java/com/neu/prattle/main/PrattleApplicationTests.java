package com.neu.prattle.main;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for PrattleApplication.
 */
public class PrattleApplicationTests {
    private PrattleApplication application;

    @Before
    public void setUp() {
        application = new PrattleApplication();
    }

    @Test
    public void testClass() {
        assertNotNull(application);
        assertNotNull(application.getClasses());
        assertEquals(5, application.getClasses().size());
    }
}
