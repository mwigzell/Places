package com.mwigzell.places.repository.api.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mwigzell.places.any
import com.mwigzell.places.model.Place
import com.mwigzell.places.repository.PlacesRepository
import com.mwigzell.places.repository.api.PlacesResponse
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class PlacesRepositoryTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var repo: PlacesRepository

    @Mock
    lateinit var networkService: NetworkService

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repo = PlacesRepository(networkService)
    }

    @Test
    fun loadPlaces() {
        val place = Place()
        place.name = PLACE_NAME
        val places = ArrayList<Place>()
        places.add(place)
        val response = PlacesResponse()
        response.status = "OK"
        response.results = places
        val observable: BehaviorSubject<PlacesResponse> = BehaviorSubject.create()
        `when`(networkService.getPlaces(any(), any(), any())).thenReturn(observable)

        val placesLd = repo.loadPlaces(LOCATION, RADIUS, TYPE_NAME)
        placesLd.observeForever() {}
        observable.onNext(response)

        assertEquals(PLACE_NAME, placesLd.value!!.get(0)!!.name)
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun setupClass() {
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> Schedulers.trampoline() }
        }

        const val LOCATION = "123.0,456.0"
        const val RADIUS = "500"
        const val TYPE_NAME = "bakery"
        const val PLACE_NAME = "Sydney"
    }
}