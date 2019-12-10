package com.mwigzell.places.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.mwigzell.places.LiveDataTestUtil
import com.mwigzell.places.capture
import com.mwigzell.places.model.PlaceLocation
import com.mwigzell.places.repository.api.LocationService
import com.mwigzell.places.repository.dao.ILocationDaoGeneric
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

class LocationRepositoryTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var repo: LocationRepository

    @Mock
    lateinit var locationService: LocationService

    @Mock
    lateinit var locationDaoGeneric: ILocationDaoGeneric

    @Captor
    lateinit var locationCaptor: ArgumentCaptor<PlaceLocation>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repo = LocationRepository(locationService, locationDaoGeneric)
    }

    @Test
    fun getLastLocation() {

        val response = PlaceLocation(LOCATION)
        val observable: BehaviorSubject<PlaceLocation> = BehaviorSubject.create()
        `when`(locationService.observeLocationChanges()).thenReturn(observable)
        `when`(locationDaoGeneric.hasLocation()).thenReturn(false)
        val locationIn: MutableLiveData<PlaceLocation> = MutableLiveData()
        `when`(locationDaoGeneric.get()).thenReturn(locationIn)

        val locationLd = repo.getLastLocation()
        observable.onNext(response)
        verify(locationDaoGeneric).insert(capture(locationCaptor))
        val placesList: PlaceLocation = locationCaptor.value
        locationIn.value = placesList

        val result = LiveDataTestUtil.getValue(locationLd)
        assertEquals(response, result)
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun setupClass() {
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> Schedulers.trampoline() }
            RxJavaPlugins.setIoSchedulerHandler { _ -> Schedulers.trampoline() }
        }

        const val LOCATION = "123.0,456.0"

    }
}