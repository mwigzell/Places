package com.mwigzell.places.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mwigzell.places.PlacesApplication

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import timber.log.Timber

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class HttpUtilsTest {

    lateinit internal var utils: HttpUtils

    @Before
    fun setup() {
        utils = HttpUtils()
    }

    @Test
    @Throws(Exception::class)
    fun testDumpUrlPlaces() {
        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&types=food&name=harbour&key=AIzaSyDYvfBnjsQttjubdoLoyj5IBvf5_x86DtI"
        utils!!.dumpUrl(url)
        Thread.sleep(5000)
    }

    @Test
    @Throws(Exception::class)
    fun testDumpUrlPhoto() {
        val photoreference = "CoQBdwAAAJdyDOJlHLNbIjDXu_KjUMXYYs9SUP-qc-380i2wvbhQs3i4DTzHU_RFordaa-4FzaTlc-gh0mD9WpyNUB6b2hN6BsCpgq1AZf_C9uZZ08IrorEgJpjmzvDWYZUQ61Y3-V5_LU3wCIb_CzKvfaZxtt7rwX6xmrliGtAkftiulPNDEhDg0FldXTL5cWqq_h36DTYXGhR6EXckzVwRqU3_vfoWem4cmZ5_Jg"
        val restaurantpic = "https://maps.googleapis.com/maps/api/place/photo?" +
                "maxwidth=400" +
                "&photoreference=" + photoreference +
                "&key=" + PlacesApplication.GOOGLE_PLACES_API_KEY

        Timber.d("Loading restaurantpic=$restaurantpic")
        utils!!.dumpUrl(restaurantpic)
        Thread.sleep(5000)
    }
}