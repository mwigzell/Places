package com.mwigzell.places.repository.dao

import androidx.lifecycle.LiveData
import com.mwigzell.places.model.PlaceLocation

/**
 * Interface to the generic location DAO implemented by multiple db tech
 */
interface ILocationDaoGeneric {

    fun hasLocation(): Boolean
    fun get(): LiveData<PlaceLocation>
    fun insert(location: PlaceLocation)
    fun delete()
}