package com.mwigzell.places.repository

import androidx.lifecycle.LiveData
import com.mwigzell.places.Mockable
import com.mwigzell.places.model.PlaceLocation
import com.mwigzell.places.repository.api.LocationService
import com.mwigzell.places.repository.dao.ILocationDaoGeneric
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Mockable
@Singleton
class LocationRepository @Inject constructor(
        val locationService: LocationService,
        val locationDaoGeneric: ILocationDaoGeneric
): Repository() {

    fun getLastLocation(): LiveData<PlaceLocation> {
        refreshLocation()
        return locationDaoGeneric.get()
    }

    private fun refreshLocation() {
        dispose()

        addDisposable(
                locationService.observeLocationChanges()
                        .observeOn(Schedulers.io()) // correct for LocationService event emitted on main
                        .map {
                            if ( locationDaoGeneric.hasLocation()) {
                                locationDaoGeneric.delete()
                            }
                            insertLocation(it).subscribe()
                            true
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { d -> Timber.d("done: $d") },
                            { e -> Timber.e(e) }
                        )
        )
    }

    private fun insertLocation(location: PlaceLocation) : Completable {
        return Completable.fromCallable() {
            locationDaoGeneric.insert(location)
            Timber.d("inserted $location in DB")
        }
    }
}