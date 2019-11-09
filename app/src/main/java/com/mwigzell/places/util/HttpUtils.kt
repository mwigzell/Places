package com.mwigzell.places.util

import com.mwigzell.places.network.ByteArrayPool
import com.mwigzell.places.network.PoolingByteArrayOutputStream

import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

import javax.inject.Inject

import timber.log.Timber

/**
 *
 */

class HttpUtils @Inject
constructor() {

    /**
     * Convenience method for use in constructing T by descendant classes.
     * Returns a byte[] which is created by parsing the input stream associated with httpURLConnection.
     *
     * @param httpURLConnection
     * @return byte[] containing the content from the "is" argument.
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun getDataBytes(httpURLConnection: HttpURLConnection): ByteArray? {
        if (pool == null) {
            pool = ByteArrayPool(DEFAULT_POOL_SIZE)
        }
        val poolBytes = PoolingByteArrayOutputStream(pool, -1)
        var buffer: ByteArray? = null
        val bytes: ByteArray
        val `is` = httpURLConnection.inputStream
        try {
            if (`is` == null) {
                throw IOException("null InputStream")
            }
            buffer = pool!!.getBuf(1024)
            while (true) {
                val count = `is`.read(buffer)
                if (count == -1) break
                poolBytes.write(buffer, 0, count)
            }
            bytes = poolBytes.toByteArray()
        } finally {
            try {
                // Close the InputStream.
                `is`?.close()
            } catch (e: IOException) {
                // This can happen if there was an exception above that left the entity in
                // an invalid state.
                Timber.d("Error occurred when calling stream close")
            }

            pool!!.returnBuf(buffer)
            poolBytes.close()
        }
        return bytes
    }

    fun dumpUrl(url: String) {
        val t = object : Thread() {
            override fun run() {
                super.run()
                try {
                    val connection = URL(url).openConnection() as HttpURLConnection
                    val statusCode = connection.responseCode
                    Timber.d("statusCode=$statusCode")
                    val bytes = getDataBytes(connection)
                    if (bytes != null && bytes.size > 0) {
                        val parsed = String(bytes, Charsets.UTF_8)
                        Timber.d(parsed)
                    }
                } catch (e: Exception) {
                    Timber.e(e, "url: $url failed")
                }

            }
        }
        t.start()
    }

    companion object {

        private val DEFAULT_POOL_SIZE = 64 * 1024
        internal var pool: ByteArrayPool? = null
    }
}
