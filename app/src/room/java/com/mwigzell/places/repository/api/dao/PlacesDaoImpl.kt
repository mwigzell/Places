package com.mwigzell.places.repository.api.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mwigzell.places.model.Place
import com.mwigzell.places.repository.PlacesDao
import com.mwigzell.places.repository.PlacesRequest
import javax.inject.Inject

/**
 * Mediate between abstract model and dto for ROOM implementation
 */
class PlacesDaoImpl @Inject constructor() : PlacesDao {
    var query: MutableLiveData<List<Place>>? = null

    override fun hasData(request: PlacesRequest): Boolean {
        return false
    }

    override fun get(request: PlacesRequest): LiveData<List<Place>> {
        query = MutableLiveData()
        return query!!
    }

    override fun insert(request: PlacesRequest, places: List<Place>) {
        query?.postValue(places)
    }
}