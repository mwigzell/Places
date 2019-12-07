package com.mwigzell.places.repository.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mwigzell.places.model.Place
import com.mwigzell.places.repository.PlacesRequest
import timber.log.Timber
import javax.inject.Inject

/**
 * Mediate between abstract model and dto for ROOM implementation
 */
class PlaceDaoGeneric @Inject constructor() : IPlaceDaoGeneric {
    var query: MutableLiveData<List<Place>>? = null

    override fun hasPlaces(request: PlacesRequest): Boolean {
        Timber.d("called")
        return false
    }

    override fun get(request: PlacesRequest): LiveData<List<Place>> {
        Timber.d("called")
        query = MutableLiveData()
        return query!!
    }

    override fun insert(request: PlacesRequest, places: List<Place>) {
        Timber.d("called")
        query?.postValue(places)
    }
}