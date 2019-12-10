package com.mwigzell.places.repository.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mwigzell.places.model.PlaceLocation
import timber.log.Timber
import javax.inject.Inject

/**
 * Mediate between abstract model and dto for ROOM implementation
 */
class LocationDaoGeneric @Inject constructor(db: PlacesDatabase) : ILocationDaoGeneric {
    val locationDao: LocationDao

    init {
        locationDao = db.getLocationDao()
    }

    override fun hasLocation(): Boolean {
        Timber.d("called")
        return locationDao.hasLocation()
    }

    override fun get(): LiveData<PlaceLocation> {
        Timber.d("called")
        val liveData = locationDao.get()

        return Transformations.switchMap(liveData) {
            val locationLiveData: MutableLiveData<PlaceLocation> = MutableLiveData()
            it?.let {
                val location = it.toLocation()
                locationLiveData.value = location
            }
            locationLiveData
        }
    }

    override fun insert(location: PlaceLocation) {
        Timber.d("called")
        locationDao.insert(fromPlaceLocation(location))
    }

    override fun delete() {
        Timber.d("called")
        locationDao.delete()
    }

    private fun fromPlaceLocation(location: PlaceLocation): LocationDto {
        return LocationDto(location.toString())
    }
}