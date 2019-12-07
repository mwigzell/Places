package com.mwigzell.places.repository.dao

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(PlaceDto::class), version = 1)
abstract class PlacesDatabase: RoomDatabase() {
    abstract fun getPlaceDao(): PlaceDao
}