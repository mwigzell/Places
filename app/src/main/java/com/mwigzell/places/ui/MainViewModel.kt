package com.mwigzell.places.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mwigzell.places.Mockable
import com.mwigzell.places.data.LocationService
import com.mwigzell.places.data.network.NetworkService
import com.mwigzell.places.model.Place
import com.mwigzell.places.model.Type
import com.mwigzell.places.redux.ActionCreator
import com.mwigzell.places.redux.AppAction
import com.mwigzell.places.redux.AppState
import com.mwigzell.places.redux.jedux.Store
import com.mwigzell.places.redux.jedux.Subscriber
import com.mwigzell.places.redux.jedux.Subscription
import javax.inject.Inject

@Mockable
open class MainViewModel @Inject constructor(
        val store: Store<AppAction<Any>, AppState>,
        val networkService: NetworkService,
        val locationService: LocationService,
        val actionCreator: ActionCreator
): ViewModel(), Subscriber {

    internal var subscription: Subscription? = null
    private val places: MutableLiveData<List<Place>> = MutableLiveData()
    private val noResults: MutableLiveData<Boolean> = MutableLiveData()
    private val types: MutableLiveData<List<Type>> = MutableLiveData()

    fun getPlaces(): LiveData<List<Place>> { return places }
    fun getNoResults(): LiveData<Boolean> { return noResults }
    fun getTypes(): LiveData<List<Type>> { return types }


    fun fetchPlaces() {
        val location = store.state.location()
        val loc: String
        if (location.longitude != 0.0 && location.latitude != 0.0) {
            loc = location.latitude.toString() + "," + location.longitude
        } else {
            loc = DEFAULT_LOCATION
        }
        var i = store.state.selectedPosition()
        var name = DEFAULT_TYPE
        if (i < store.state.types().size)
            name = store.state.types().get(i).name
        networkService!!.getPlaces(loc, DEFAULT_RADIUS, name)
    }

    fun loadTypes() {
        actionCreator.loadTypes()
    }

    fun onResume() {
        subscription = store.subscribe(this)
    }

    fun onPause() {
        subscription?.unsubscribe()
    }

    override fun onStateChanged() {
        val state = store.state.state()
        when (state) {
            AppState.States.PLACES_DOWNLOADED -> {
                places.setValue(store.state.placeState().places)
            }
            AppState.States.GET_PLACES_FAILED -> noResults.setValue(true)
            AppState.States.LOCATION_UPDATED -> {
            }
            AppState.States.TYPES_LOADED -> {
               types.setValue(store.state.types())
            }
        }
    }

    companion object {
        val DEFAULT_LOCATION = "-33.8670522,151.1957362"
        val DEFAULT_TYPE = "food&name=harbour"
        val DEFAULT_RADIUS = "5000"
    }
}