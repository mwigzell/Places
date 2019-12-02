package com.mwigzell.places.repository.api.network

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.mwigzell.places.TestApplication
import com.mwigzell.places.TestUtil
import com.mwigzell.places.repository.PlacesRequest
import com.mwigzell.places.repository.api.PlacesResponse
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import timber.log.Timber
import javax.inject.Inject

class NetworkServiceTest {
    @Inject
    lateinit var networkService: NetworkService

    lateinit private var webServer: MockWebServer
    lateinit private var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        val app = context.applicationContext as TestApplication
        app.getInjector().inject(this)
        webServer = MockWebServer()
        webServer.start(8080)
    }

    @After
    fun teardown() {
        webServer.shutdown()
    }

    @Test
    fun testGetPlaces() {
        val response = TestUtil().getStringFromResource("places.json")
        webServer.enqueue(MockResponse().setBody(response))

        var placesResponse: PlacesResponse? = null
        networkService.getPlaces(PlacesRequest(LOCATION, RADIUS, TYPE_NAME))
                .subscribe (
                {
                    placesResponse = it
                },{ e ->
                    Timber.e(e)
                })
        assertNotNull(placesResponse)
        assertEquals("OK", placesResponse!!.status)
        assertNotNull(placesResponse!!.results)
    }

    companion object {
        const val LOCATION = "123.0,456.0"
        const val RADIUS = "500"
        const val TYPE_NAME = "bakery"
    }
}