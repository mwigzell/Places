package com.mwigzell.places.repository

import androidx.lifecycle.LiveData
import com.mwigzell.places.model.Place

interface PlacesDao {
    fun hasData(request: PlacesRequest): Boolean
    fun get(request: PlacesRequest): LiveData<List<Place>>
    fun insert(request: PlacesRequest, places: List<Place>)
}