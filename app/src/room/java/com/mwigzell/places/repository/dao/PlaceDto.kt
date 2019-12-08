package com.mwigzell.places.repository.dao

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mwigzell.places.model.Place

data class PlaceKey constructor(
      val latlong: String,
      val radius: String,
      val type: String,
      val name: String
)


@Entity(tableName = "place", primaryKeys = [ "latlong", "radius", "type", "name"] )
data class PlaceDto @JvmOverloads constructor(
        @Embedded
        val id: PlaceKey,
        val photoReference: String
) {
    fun toPlace(): Place {
        val place = Place()
        place.name = id.name
        val photos: ArrayList<Place.Photo> = ArrayList()
        val photo = place.Photo()
        photo.photoReference = photoReference
        photos.add(photo)
        place.photos = photos
        return place
    }
}