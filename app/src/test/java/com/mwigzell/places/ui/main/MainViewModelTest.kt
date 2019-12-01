package com.mwigzell.places.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.mwigzell.places.any
import com.mwigzell.places.model.Place
import com.mwigzell.places.model.PlaceLocation
import com.mwigzell.places.model.Type
import com.mwigzell.places.repository.PlacesRepository
import com.mwigzell.places.repository.TypesRepository
import com.mwigzell.places.repository.api.LocationService
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class MainViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var locationService: LocationService

    @Mock
    lateinit var typesRepository: TypesRepository

    @Mock
    lateinit var placesRepository: PlacesRepository

    lateinit var viewModel: MainViewModel
    lateinit var location: BehaviorSubject<PlaceLocation>
    val placesLiveData: MutableLiveData<List<Place>> = MutableLiveData()
    val typesLiveData: MutableLiveData<List<Type>> = MutableLiveData()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val placeLocation = PlaceLocation(LATLONG)
        location = BehaviorSubject.createDefault(placeLocation)
        `when`(locationService.getDefaultLocation())
                .thenReturn(placeLocation)
        `when`(locationService.observeLocationChanges())
                .thenReturn(location)
        `when`(placesRepository.loadPlaces(any(), any(), any())).thenReturn(placesLiveData)
        `when`(typesRepository.loadTypes()).thenReturn(typesLiveData)
        viewModel = MainViewModel(
                typesRepository,
                placesRepository,
                locationService)
    }

    @Test
    fun testConstruction() {
        verify(placesRepository)
                .loadPlaces(LATLONG, MainViewModel.DEFAULT_RADIUS, MainViewModel.DEFAULT_TYPE)
    }

    private fun postPlaces() {
        val places = ArrayList<Place>()
        val place = Place()
        place.name = PLACE_NAME
        places.add(place)
        placesLiveData.value = places
    }

    @Test
    fun testTypeSelected_withNewLocation() {

        location.onNext(PlaceLocation(LATLONG2))

        viewModel.onTypeSelected(Type(TYPE_NAME))
        postPlaces()

        verify(placesRepository)
                .loadPlaces(LATLONG2, MainViewModel.DEFAULT_RADIUS, TYPE_NAME)
    }

    @Test
    fun testGetPlaces() {
        postPlaces()
        var places: List<Place>? = null
        viewModel.getPlaces().observeForever { places = it}
        assertNotNull(places)
        assertEquals(PLACE_NAME, places?.get(0)?.name)
    }

    @Test
    fun testGetTypes() {
        val type = Type(TYPE_NAME)
        val types = ArrayList<Type>()
        types.add(type)
        typesLiveData.value = types
        var typesData: List<Type>? = null
        viewModel.getTypes().observeForever { typesData = it}
        assertNotNull(typesData)
        assertEquals(TYPE_NAME, typesData?.get(0)?.name)
    }

    @Test
    fun testGetSelectedType() {
        val type = Type(TYPE_NAME)
        viewModel.onTypeSelected(type)
        var typeData: Type? = null
        viewModel.getSelectedType().observeForever {typeData = it }
        assertEquals(TYPE_NAME, typeData?.name)
    }

    companion object {
        const val LATLONG = "123.0,456.0"
        const val LATLONG2 = "123.0,456.0"
        const val TYPE_NAME = "bakery"
        const val PLACE_NAME = "Sydney"
    }
}