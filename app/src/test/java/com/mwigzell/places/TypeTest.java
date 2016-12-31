package com.mwigzell.places;

import com.mwigzell.places.model.Type;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 *
 */

public class TypeTest {

    @Test
    public void create() {
        String line = "a_name an_url";
        Type t = new Type(line);

        assertEquals("a_name", t.name);
        assertEquals("an_url", t.url);
    }

    @Test
    public void createIncomplete() {
        String line = "a_name";
        Type t = new Type(line);
        assertEquals("a_name", t.name);
        assertEquals(null, t.url);
    }

    @Test
    public void createEmpty() {
        String line = "";
        Type t = new Type(line);
        assertEquals(null, t.name);
        assertEquals(null, t.url);
    }
}
