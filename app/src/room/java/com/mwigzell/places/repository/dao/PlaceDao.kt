package com.mwigzell.places.repository.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlaceDao {
    //@Query("SELECT COUNT(*) FROM place WHERE name == :name AND last_update  >= :timeout)
    @Query("SELECT COUNT(*) FROM place")
    fun hasPlaces(): Int

    @Query("SELECT id, name, photoreference FROM place")
    fun get(): LiveData<List<PlaceDto>>

    @Insert
    fun insert(places: List<PlaceDto>)
}