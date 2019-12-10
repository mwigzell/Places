package com.mwigzell.places.repository.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mwigzell.places.model.PlaceLocation

@Dao
interface LocationDao {
    @Query("SELECT COUNT(*) FROM location")
    fun hasLocation(): Boolean

    @Query("SELECT latlong FROM location")
    fun get(): LiveData<LocationDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(location: LocationDto)

    @Query("DELETE FROM location")
    fun delete(): Int
}