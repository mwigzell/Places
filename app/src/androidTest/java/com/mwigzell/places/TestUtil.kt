package com.mwigzell.places

import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class TestUtil {

    /**
     * Read out the InputStream and return the contents as a String.
     *
     * @param is
     * @return the stream contents as a String.
     * @throws java.io.IOException
     */
    @Throws(IOException::class)
    fun getString(`is`: InputStream?): String {
        var s: String? = null

        val isr = InputStreamReader(`is`)
        val sb = StringBuilder()
        while (true) {
            var c = isr.read()
            if (c == -1) {
                break;
            }
            sb.append(c.toChar())
        }
        s = sb.toString()


        return s
    }

    fun getInputStream(fileName: String): InputStream? {
        return javaClass.classLoader.getResourceAsStream(fileName)
    }

    @Throws(Exception::class)
    fun getStringFromResource(fileName: String): String {
        val `is` = getInputStream(fileName)
        return getString(`is`)
    }

    @Throws(Exception::class)
    fun getJsonFromResource(filename: String): JSONObject {
        val `is` = javaClass.classLoader.getResourceAsStream(filename)
        return JSONObject(getString(`is`))
    }
}