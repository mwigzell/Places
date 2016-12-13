package com.mwigzell.places.util;

import com.mwigzell.places.network.ByteArrayPool;
import com.mwigzell.places.network.PoolingByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Inject;

import timber.log.Timber;

/**
 *
 */

public class HttpUtils {
    @Inject
    public HttpUtils() {
    }

    private static int DEFAULT_POOL_SIZE = 64 * 1024;
    static ByteArrayPool pool;

    /**
     * Convenience method for use in constructing T by descendant classes.
     * Returns a byte[] which is created by parsing the input stream associated with httpURLConnection.
     *
     * @param httpURLConnection
     * @return byte[] containing the content from the "is" argument.
     * @throws IOException
     */
    private byte[] getDataBytes(HttpURLConnection httpURLConnection) throws IOException {
        if (pool == null) {
            pool = new ByteArrayPool(DEFAULT_POOL_SIZE);
        }
        PoolingByteArrayOutputStream poolBytes = new PoolingByteArrayOutputStream(pool, -1);
        byte[] buffer = null;
        byte[] bytes;
        InputStream is = httpURLConnection.getInputStream();
        try {
            if (is == null) {
                throw new IOException("null InputStream");
            }
            buffer = pool.getBuf(1024);
            int count;
            while ((count = is.read(buffer)) != -1) {
                poolBytes.write(buffer, 0, count);
            }
            bytes = poolBytes.toByteArray();
        } finally {
            try {
                // Close the InputStream.
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                // This can happen if there was an exception above that left the entity in
                // an invalid state.
                Timber.d("Error occurred when calling stream close");
            }
            pool.returnBuf(buffer);
            poolBytes.close();
        }
        return bytes;
    }

    public void dumpUrl(final String url) {
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    int statusCode = connection.getResponseCode();
                    Timber.d("statusCode=" + statusCode);
                    byte[] bytes = getDataBytes(connection);
                    if (bytes != null && bytes.length > 0) {
                        String parsed = new String(bytes, "UTF-8");
                        Timber.d(parsed);
                    }
                } catch (Exception e) {
                    Timber.e(e, "url: " + url + " failed");
                }
            }
        };
        t.start();
    }
}
