package com.mwigzell.places.repository.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "place")
data class PlaceDto @JvmOverloads constructor(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val name: String,
        val photoReference: String
)