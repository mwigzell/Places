package com.mwigzell.places.network

import com.mwigzell.places.PlacesApplication
//import com.mwigzell.places.dagger.Injection;
import com.mwigzell.places.model.PlacesResponse
import com.mwigzell.places.redux.ActionCreator
import com.mwigzell.places.redux.AppAction
import com.mwigzell.places.redux.AppState
import com.mwigzell.places.redux.jedux.Store
import com.mwigzell.places.redux.original.Subscriber

import javax.inject.Inject
import javax.inject.Singleton

import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber

@Singleton
class NetworkService @Inject
constructor(private val serviceCreator: ServiceCreator,
            private val actionCreator: ActionCreator,
            private val store: Store<AppAction<Any>, AppState>) : Subscriber {

    private val client: ServiceCreator.PlacesClient

    init {
        store!!.subscribe(this)
        client = serviceCreator!!.createService(ServiceCreator.PlacesClient::class.java)
    }

    @JvmOverloads
    fun getPlaces(location: String = "-33.8670522,151.1957362", radius: String = "500", type: String = "food&name=harbour") {
        Timber.d("Get places radius=$radius type=$type")

        client.getPlaces(location, radius, type, PlacesApplication.GOOGLE_PLACES_API_KEY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : rx.Subscriber<PlacesResponse>() {
                    override fun onCompleted() {
                        Timber.d("  completed")
                    }

                    override fun onError(e: Throwable) {
                        Timber.e(e, "  error with exception")
                        actionCreator!!.getPlacesFailed(e)
                    }

                    override fun onNext(response: PlacesResponse) {
                        Timber.d("Got places status:" + response.status)
                        actionCreator!!.placesDownloaded(response.results)
                    }
                })
    }

    override fun onStateChanged() {
        val state = store!!.state.state()
        //Timber.d("State Changed: %s",state.name());
        if (state == AppState.States.GET_PLACES) {
            getPlaces()
        }
    }

    fun clearCache() {
        serviceCreator!!.clearCacheForOkHTTP()
    }

    fun close() {
        serviceCreator!!.clearCacheForOkHTTP()
        serviceCreator!!.close()
    }
}
