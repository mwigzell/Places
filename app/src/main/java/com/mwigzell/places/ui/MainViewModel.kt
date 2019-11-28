package com.mwigzell.places.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mwigzell.places.Mockable
import com.mwigzell.places.data.DataService
import com.mwigzell.places.data.LocationService
import com.mwigzell.places.data.network.NetworkService
import com.mwigzell.places.model.Place
import com.mwigzell.places.model.PlaceLocation
import com.mwigzell.places.model.Type
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

//TODO: persist last type selected

@Mockable
class MainViewModel @Inject constructor(
        val networkService: NetworkService,
        val locationService: LocationService,
        val dataService: DataService
): ViewModel() {

    private val places: MutableLiveData<List<Place>> = MutableLiveData()
    private val noResults: MutableLiveData<Boolean> = MutableLiveData()
    private val types: MutableLiveData<List<Type>> = MutableLiveData()
    private lateinit var location: PlaceLocation
    private var type: Type? = null

    init {
       location = locationService.getDefaultLocation()
        locationService.observeLocationChanges()
                .subscribe {
                    location = it
                    Timber.d("Got new location $it")
                }
        fetchPlaces()
    }

    fun getPlaces(): LiveData<List<Place>> { return places }
    fun getNoResults(): LiveData<Boolean> { return noResults }
    fun getTypes(): LiveData<List<Type>> { return types }

    fun fetchPlaces() {
        var name = DEFAULT_TYPE
        type?.let { name = it.name }
        networkService.getPlaces(location.toString(), DEFAULT_RADIUS, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    Timber.e(it, "<--  error with exception")
                    noResults.setValue(true)
                }
                .subscribe {
                    Timber.d("<-- Got places status:" + it.status)
                    places.setValue(it.results)
                }
    }

    fun loadTypes() {
        dataService.fetchTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    Timber.e(it)}
                .subscribe {
                    types.setValue(it)
                }
    }

    fun onTypeSelected(type: Type) {
        this.type = type
        fetchPlaces()
    }

    companion object {
        val DEFAULT_LOCATION = LocationService.DEFAULT_LOCATION
        val DEFAULT_TYPE = "food&name=harbour"
        val DEFAULT_RADIUS = "5000"
    }
}