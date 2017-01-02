package com.mwigzell.places.model;

import static junit.framework.Assert.assertTrue;

/**
 * Represent a Google Type representation read from file.
 * The file consists of a line per type with <type>\b<url>
 */

public class Type {
    public final String name;
    public final String url;

    public Type(String line) {
        String s[] = line.split("\\s+");

        if (s.length >= 1 && s[0].length() > 0)
            name = s[0];
        else
            name = null;
        if (s.length >= 2)
            url = s[1];
        else
            url = null;
    }

    public String toString() {
        return name;
    }
}
