package com.neu.prattle.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TranslatorTests {
    private Translator test;

    @Before
    public void setUp() {
        test = new Translator();
    }

    @Test
    public void testTranslation() {
        assertEquals("¡Hola Mundo!", test.translate("Hello, world!", "es"));
        assertEquals("Hello World!", test.translate("¡Hola mundo!", "en"));
    }
}