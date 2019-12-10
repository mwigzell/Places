package com.mwigzell.places.repository.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mwigzell.places.model.Place

@Entity(tableName = "place")
data class PlaceDto constructor(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val latlong: String,
        val radius: String,
        val type: String,
        val name: String,
        val photoReference: String
) {
    fun toPlace(): Place {
        val place = Place()
        place.name = name
        val photos: ArrayList<Place.Photo> = ArrayList()
        val photo = place.Photo()
        photo.photoReference = photoReference
        photos.add(photo)
        place.photos = photos
        return place
    }
}