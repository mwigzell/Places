package com.mwigzell.places.model;

import com.mwigzell.places.model.Type;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 *
 */

public class TypeTest {

    @Test
    public void testCreate() {
        String line = "a_name an_url";
        Type t = new Type(line);

        assertEquals("a_name", t.name);
        assertEquals("an_url", t.url);
    }

    @Test
    public void testCreateIncomplete() {
        String line = "a_name";
        Type t = new Type(line);
        assertEquals("a_name", t.name);
        assertEquals(null, t.url);
    }

    @Test
    public void testCreateEmpty() {
        String line = "";
        Type t = new Type(line);
        assertEquals(null, t.name);
        assertEquals(null, t.url);
    }
}
