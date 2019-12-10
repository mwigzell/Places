package com.mwigzell.places.repository.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mwigzell.places.model.PlaceLocation

@Entity(tableName = "location")
data class LocationDto constructor(
       @PrimaryKey
       val latlong: String
) {
    fun toLocation(): PlaceLocation {
        return PlaceLocation(latlong)
    }
}