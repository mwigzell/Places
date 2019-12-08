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
        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&types=food&name=harbour&key=" + PlacesApplication.GOOGLE_PLACES_API_KEY
        utils!!.dumpUrl(url)
        Thread.sleep(5000)
    }

    @Test
    @Throws(Exception::class)
    fun testDumpUrlPhoto() {
        val photoreference = "CmRaAAAA8vEs39GnPpLhvMBgkVfuvSDwwN4q829I2ypi9aJjoOkzlKGmVbpYnU7ng5Bm1Js3cPVzGpPrOQZC_Chx__Gv5nk0FFbKyCamVbxgemml1X0JnX44f68b0Oiu76OUmb-uEhAWujEbnLMH7aUkrqmxsnSzGhRYAbXUs1z8nPctAoEKQgtQGm_7dQ"
        val restaurantpic = "https://maps.googleapis.com/maps/api/place/photo?" +
                "maxwidth=400" +
                "&photoreference=" + photoreference +
                "&key=" + PlacesApplication.GOOGLE_PLACES_API_KEY

        Timber.d("Loading restaurantpic=$restaurantpic")
        utils!!.dumpUrl(restaurantpic)
        Thread.sleep(5000)
    }
}