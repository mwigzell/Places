package com.mwigzell.places.repository.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.mwigzell.places.observeOnce
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LocationDaoGenericTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var locationDao: LocationDao
    private lateinit var db: PlacesDatabase
    private lateinit var locationDaoGeneric: LocationDaoGeneric

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, PlacesDatabase::class.java).build()
        locationDao = db.getLocationDao()
        locationDaoGeneric = LocationDaoGeneric(db)
    }

    @After
    fun teardown() {
        db.close()
    }

    private fun buildData(): LocationDto {
        val locationDto = LocationDto("123.0,456.0")

        return locationDto
    }

    fun insert(): LocationDto {
        val locationDto = buildData()
        locationDao.insert(locationDto)
        return locationDto
    }

    @Test
    fun hasLocation() {
        insert()

        assertTrue(locationDaoGeneric.hasLocation())
    }

    @Test
    fun get() {
        val locationDto = insert()

        val liveData = locationDaoGeneric.get()

        liveData.observeOnce { assertEquals(locationDto.latlong, it.toString()) }
    }

    @Test
    fun insertLocation() {
        val locationDto = buildData()

        locationDaoGeneric.insert(locationDto.toLocation())
        assertTrue(locationDaoGeneric.hasLocation())
    }

    @Test
    fun insertDelete() {
        val locationDto = buildData()

        locationDaoGeneric.insert(locationDto.toLocation())
        locationDaoGeneric.delete()
        assertFalse(locationDaoGeneric.hasLocation())
    }
}