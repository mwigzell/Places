package com.mwigzell.places.repository

import androidx.lifecycle.LiveData
import com.mwigzell.places.model.Place

interface PlacesDao {
    fun hasData(location: String, radius: String, type: String): List<Place>
    fun get(location: String, radius: String, type: String): LiveData<List<Place>>
    fun insert(location: String, radius: String, type: String, places: List<Place>)
}