package com.mwigzell.places.data.network

import com.mwigzell.places.PlacesApplication
import com.mwigzell.places.model.PlacesResponse
import com.mwigzell.places.redux.ActionCreator
import com.mwigzell.places.redux.AppAction
import com.mwigzell.places.redux.AppState
import com.mwigzell.places.redux.jedux.Store
import rx.Observable
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkService @Inject
constructor(private val serviceCreator: ServiceCreator) {

    private val client: ServiceCreator.PlacesClient

    init {
        client = serviceCreator.createService(ServiceCreator.PlacesClient::class.java)
    }

    @JvmOverloads
    fun getPlaces(location: String = "-33.8670522,151.1957362",
                  radius: String = "500",
                  type: String = "food&name=harbour"): Observable<PlacesResponse> {
        Timber.d("--> Get places loc=$location radius=$radius type=$type")

        return client.getPlaces(location, radius, type, PlacesApplication.GOOGLE_PLACES_API_KEY)
    }

    fun clearCache() {
        serviceCreator.clearCacheForOkHTTP()
    }

    fun close() {
        serviceCreator.clearCacheForOkHTTP()
        serviceCreator.close()
    }
}
