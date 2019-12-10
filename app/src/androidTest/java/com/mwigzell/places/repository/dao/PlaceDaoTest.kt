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
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, PlacesDatabase::class.java).build()
        placeDao = db.getPlaceDao()
    }

    @After
    @Throws(IOException::class)
    fun teardown() {
        db.close()
    }

    private fun insertData(type: String = PlaceDaoTest.TYPE): List<PlaceDto> {
        val list = ArrayList<PlaceDto>()
        list.add(PlaceTestUtil.create("george", "a photo ref", type))
        list.add(PlaceTestUtil.create("harry", "a photo ref2", type))
        placeDao.insert(list)
        return list
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        val list = insertData()
        insertData(TYPE2)

        assertEquals(2, placeDao.hasPlaces(LATLONG, RADIUS, TYPE))

        val placesLiveData = placeDao.get(LATLONG, RADIUS, TYPE)
        var places: List<PlaceDto>? = null
        placesLiveData.observeOnce {places = it }

        assertEquals(2, places!!.size)
        for (i in 0..1) {
            assertThat(places!!.get(i).latlong, equalTo(list.get(i).latlong))
            assertThat(places!!.get(i).radius, equalTo(list.get(i).radius))
            assertThat(places!!.get(i).type, equalTo(list.get(i).type))
            assertThat(places!!.get(i).name, equalTo(list.get(i).name))
            assertThat(places!!.get(i).photoReference, equalTo(list.get(i).photoReference))
        }
    }

    @Test
    fun deleteRow() {
        insertData()
        insertData(TYPE2)

        val count = placeDao.delete(LATLONG, RADIUS, TYPE)

        assertEquals(2, count)
        assertEquals(2, placeDao.hasPlaces(LATLONG, RADIUS, TYPE2))
    }

    companion object {
        const val LATLONG = PlaceTestUtil.LATLONG
        const val RADIUS = PlaceTestUtil.RADIUS
        const val TYPE = PlaceTestUtil.TYPE
        const val TYPE2 = PlaceTestUtil.TYPE2
    }
}