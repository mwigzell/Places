package com.mwigzell.places.repository.dao

import androidx.lifecycle.LiveData
import com.mwigzell.places.model.Place
import com.mwigzell.places.repository.PlacesRequest

/**
 * Interface to the generic place DAO which is implemented by multiple DB tech
 */
interface IPlaceDaoGeneric {
    fun hasPlaces(request: PlacesRequest): Boolean
    fun get(request: PlacesRequest): LiveData<List<Place>>
    fun insert(request: PlacesRequest, places: List<Place>)
}