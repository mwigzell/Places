package com.mwigzell.places.repository.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.mwigzell.places.observeOnce
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class PlaceDaoTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var placeDao: PlaceDao
    private lateinit var db: PlacesDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
                context, PlacesDatabase::class.java).build()
        placeDao = db.getPlaceDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        val list = ArrayList<PlaceDto>()
        list.add(PlaceDto(1, "george", "a photo ref"))
        list.add(PlaceDto(2, "harry", "a photo ref2"))
        placeDao.insert(list)

        assertEquals(2, placeDao.hasPlaces())

        val byName = placeDao.get()
        var places: List<PlaceDto>? = null
        byName.observeOnce {places = it }

        assertThat(places!!.get(0), equalTo(list.get(0)))
    }
}