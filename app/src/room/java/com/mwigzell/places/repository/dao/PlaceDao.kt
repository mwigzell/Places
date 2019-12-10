package com.mwigzell.places.repository.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mwigzell.places.repository.PlacesRequest

//TODO: add a time to live mechanism (check against timestamp)
@Dao
interface PlaceDao {
    //@Query("SELECT COUNT(*) FROM place WHERE name == :name AND last_update  >= :timeout)
    @Query("SELECT COUNT(*) FROM place WHERE latlong == :latlong AND radius == :radius AND type == :type")
    fun hasPlaces(latlong: String, radius: String, type: String): Int

    @Query("SELECT id, latlong, radius, type, name, photoReference " +
            "FROM place WHERE latlong == :latlong AND radius == :radius AND type == :type")
    fun get(latlong: String, radius: String, type: String): LiveData<List<PlaceDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(places: List<PlaceDto>)

    @Query("DELETE FROM place WHERE latlong == :latlong AND radius == :radius AND type == :type")
    fun delete(latlong: String, radius: String, type: String): Int
}