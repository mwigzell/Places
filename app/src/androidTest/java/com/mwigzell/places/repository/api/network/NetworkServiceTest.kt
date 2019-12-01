package com.mwigzell.places.repository.api.network

import com.mwigzell.places.TestUtil
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.QueueDispatcher
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Test

class NetworkServiceTest {
    val dispatcher: QueueDispatcher = object : QueueDispatcher() {

        @Throws(InterruptedException::class)
        override fun dispatch(request: RecordedRequest): MockResponse {

            when (request.path) {
                "/maps/api/place/nearbysearch/json" -> {
                    val response = TestUtil().getStringFromResource("places.json")
                    return MockResponse().setResponseCode(200).setBody(response)
                }
            }
            return MockResponse().setResponseCode(404)
        }
    }

    lateinit private var webServer: MockWebServer
    lateinit var networkService: NetworkService

    @Before
    fun setUp() {
        webServer = MockWebServer()
        webServer.start(8080)
        webServer.dispatcher = dispatcher
        networkService = NetworkService(ServiceCreator())
    }

    @After
    fun teardown() {
        webServer.shutdown()
    }

    @Test
    fun testGetPlaces() {

    }
}