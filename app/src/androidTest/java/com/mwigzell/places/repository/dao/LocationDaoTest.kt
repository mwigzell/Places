package com.mwigzell.places.repository.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.mwigzell.places.observeOnce
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LocationDaoTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var locationDao: LocationDao
    private lateinit var db: PlacesDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, PlacesDatabase::class.java).build()
        locationDao = db.getLocationDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    private fun insertData(): LocationDto {
        val locationDto = LocationDto("123.0,456.0")

        locationDao.insert(locationDto)

        return locationDto
    }

    @Test
    fun insertAndGet() {
        val locationDto = insertData()

        assertEquals(true, locationDao.hasLocation())

        var locationLiveData = locationDao.get()
        locationLiveData.observeOnce {
            assertEquals(locationDto, it)
        }
    }

    @Test
    fun deleteRow() {
        insertData()

        val count = locationDao.delete()

        assertEquals(1, count)
    }
}