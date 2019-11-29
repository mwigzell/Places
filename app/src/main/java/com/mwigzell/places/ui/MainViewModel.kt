package com.mwigzell.places.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mwigzell.places.Mockable
import com.mwigzell.places.repository.LocationService
import com.mwigzell.places.repository.network.NetworkService
import com.mwigzell.places.model.Place
import com.mwigzell.places.model.PlaceLocation
import com.mwigzell.places.model.Type
import com.mwigzell.places.repository.PlacesRepository
import com.mwigzell.places.repository.TypesRepository
import timber.log.Timber
import javax.inject.Inject

//TODO: persist last type selected

@Mockable
class MainViewModel @Inject constructor(
        val typesRepository: TypesRepository,
        val placesRepository: PlacesRepository,
        val networkService: NetworkService,
        val locationService: LocationService
): ViewModel() {

    private val places: MediatorLiveData<List<Place>> = MediatorLiveData()
    private val types: LiveData<List<Type>> = typesRepository.loadTypes()
    private lateinit var location: PlaceLocation
    private var type: Type? = null
    private var placesSource: LiveData<List<Place>>? = null

    init {
       location = locationService.getDefaultLocation()
        locationService.observeLocationChanges()
                .subscribe {
                    location = it
                    Timber.d("Got new location $it")
                }
        loadPlaces()
    }

    fun getPlaces(): LiveData<List<Place>> { return places }
    fun getTypes(): LiveData<List<Type>> { return types }

    fun loadPlaces() {
        var name = DEFAULT_TYPE
        type?.let { name = it.name }
        val placesLiveData = placesRepository.loadPlaces(location.toString(), DEFAULT_RADIUS, name)
        placesSource?.let { places.removeSource(it)}
        places.addSource(placesLiveData) {
            places.value = it
        }
    }

    fun onTypeSelected(type: Type) {
        this.type = type
        loadPlaces()
    }

    companion object {
        val DEFAULT_LOCATION = LocationService.DEFAULT_LOCATION
        val DEFAULT_TYPE = "food&name=harbour"
        val DEFAULT_RADIUS = "5000"
    }
}