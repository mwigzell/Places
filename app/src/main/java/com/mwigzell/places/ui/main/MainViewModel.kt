package com.mwigzell.places.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.SavedStateHandle
import com.mwigzell.places.Mockable
import com.mwigzell.places.model.Place
import com.mwigzell.places.model.PlaceLocation
import com.mwigzell.places.model.Type
import com.mwigzell.places.repository.LocationRepository
import com.mwigzell.places.repository.PlacesRepository
import com.mwigzell.places.repository.PlacesRequest
import com.mwigzell.places.repository.TypesRepository
import com.mwigzell.places.ui.BaseViewModel
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

//TODO: persist last type selected

@Mockable
@Singleton
class MainViewModel @Inject constructor(
        //val savedStateHandle: SavedStateHandle,
        val typesRepository: TypesRepository,
        val placesRepository: PlacesRepository,
        val locationRepository: LocationRepository
): BaseViewModel() {

    private val places: MediatorLiveData<List<Place>> = MediatorLiveData()
    private val types: LiveData<List<Type>> = typesRepository.loadTypes()
    private lateinit var location: PlaceLocation
    private val selectedType: MutableLiveData<Type> = MutableLiveData()
    private var placesSource: LiveData<List<Place>>? = null
    lateinit private var locationSource: LiveData<PlaceLocation>
    private var loadedPlaces = false

    init {
        locationSource = locationRepository.getLastLocation()
        places.addSource(locationSource) {
            location = it
            Timber.d("Got new location $it")
            if (places.value == null && !loadedPlaces) {
                loadPlaces()
                loadedPlaces = true
            }
        }
    }

    fun getPlaces(): LiveData<List<Place>> { return places }
    fun getTypes(): LiveData<List<Type>> { return types }
    fun getSelectedType(): LiveData<Type> { return selectedType }

    private fun loadPlaces() {
        var name = DEFAULT_TYPE
        selectedType.value?.let { name = it.name }
        val placesLiveData = placesRepository.loadPlaces(
                PlacesRequest(location.toString(), DEFAULT_RADIUS, name))
        placesSource?.let { places.removeSource(it)}
        places.addSource(placesLiveData) {
            places.value = it
        }
        placesSource = placesLiveData
    }

    fun onTypeSelected(type: Type) {
        this.selectedType.value = type
        loadPlaces()
    }

    companion object {
        val DEFAULT_TYPE = "food&name=harbour"
        val DEFAULT_RADIUS = "5000"
    }
}