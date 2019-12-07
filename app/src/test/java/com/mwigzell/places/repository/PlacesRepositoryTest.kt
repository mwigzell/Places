package com.mwigzell.places.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.mwigzell.places.LiveDataTestUtil
import com.mwigzell.places.any
import com.mwigzell.places.capture
import com.mwigzell.places.model.Place
import com.mwigzell.places.repository.api.PlacesResponse
import com.mwigzell.places.repository.api.network.NetworkService
import com.mwigzell.places.repository.dao.IPlaceDaoGeneric
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class PlacesRepositoryTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var repo: PlacesRepository

    @Mock
    lateinit var networkService: NetworkService

    @Mock
    lateinit var IPlaceDaoGeneric: IPlaceDaoGeneric

    @Captor
    lateinit var placesCaptor: ArgumentCaptor<List<Place>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repo = PlacesRepository(networkService, IPlaceDaoGeneric)
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
        `when`(networkService.getPlaces(any())).thenReturn(observable)

        `when`(IPlaceDaoGeneric.hasPlaces(any())).thenReturn(false)
        val placesIn: MutableLiveData<List<Place>> = MutableLiveData()
        `when`(IPlaceDaoGeneric.get(any())).thenReturn(placesIn)
        val placesLd = repo.loadPlaces(PlacesRequest(LOCATION, RADIUS, TYPE_NAME))
        observable.onNext(response)

        verify(IPlaceDaoGeneric).insert(any(), capture(placesCaptor))
        val placesList: List<Place> = placesCaptor.value
        placesIn.value = placesList

        val result =  LiveDataTestUtil.getValue(placesLd)

        assertEquals(PLACE_NAME, result.get(0)!!.name)
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun setupClass() {
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> Schedulers.trampoline() }
            RxJavaPlugins.setIoSchedulerHandler { _ -> Schedulers.trampoline() }
        }

        const val LOCATION = "123.0,456.0"
        const val RADIUS = "500"
        const val TYPE_NAME = "bakery"
        const val PLACE_NAME = "Sydney"
    }
}