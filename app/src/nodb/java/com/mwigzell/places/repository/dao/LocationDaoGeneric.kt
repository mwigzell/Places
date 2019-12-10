package com.mwigzell.places.repository.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mwigzell.places.model.PlaceLocation
import timber.log.Timber
import javax.inject.Inject

/**
 * Mediate between abstract model and dto for NODB implementation
 */
class LocationDaoGeneric @Inject constructor() : ILocationDaoGeneric {
    var query: MutableLiveData<PlaceLocation>? = null

    override fun hasLocation(): Boolean {
        Timber.d("called")
        return false
    }

    override fun get(): LiveData<PlaceLocation> {
        Timber.d("called")
        query = MutableLiveData()
        return query!!
    }

    override fun insert(location: PlaceLocation) {
        Timber.d("called")
        query?.postValue(location)
    }

    override fun delete() {
        Timber.d("called")
    }
}