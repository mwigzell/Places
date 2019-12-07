package com.mwigzell.places.repository.api.network

import TestFileUtil
import com.mwigzell.places.dagger.AppModule
import com.mwigzell.places.repository.PlacesRequest
import com.mwigzell.places.repository.api.PlacesResponse
import com.mwigzell.places.util.FileUtils
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import timber.log.Timber
import java.io.File

class NetworkServiceTest {

    lateinit var networkService: NetworkService

    lateinit private var webServer: MockWebServer

    @Before
    fun setUp() {
        networkService = NetworkService(ServiceCreator(
                File("/tmp"),
                FileUtils(),
                object: NetworkStatus {
                    override val isNetwork: Boolean
                        get() = true
                },
                RETROFIT_BASE_URL
        ))
        webServer = MockWebServer()
        webServer.start(8080)
    }

    @After
    fun teardown() {
        webServer.shutdown()
    }

    @Test
    fun testGetPlaces() {
        val response = TestFileUtil().getStringFromResource("places.json")
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
        const val RETROFIT_BASE_URL = "http://localhost:8080/"
    }
}