package com.mwigzell.places.repository.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.mwigzell.places.model.Place
import com.mwigzell.places.observeOnce
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class PlaceDaoGenericTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var placeDao: PlaceDao
    private lateinit var db: PlacesDatabase
    private lateinit var placeDaoGeneric: PlaceDaoGeneric

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, PlacesDatabase::class.java).build()
        placeDao = db.getPlaceDao()
        placeDaoGeneric = PlaceDaoGeneric(db)
    }

    @After
    @Throws(IOException::class)
    fun teardown() {
        db.close()
    }

    fun buildList(type: String = PlaceDaoTest.TYPE): List<PlaceDto> {
        val list = ArrayList<PlaceDto>()
        list.add(PlaceTestUtil.create("george", "a photo ref", type))
        list.add(PlaceTestUtil.create("harry", "a photo ref2", type))
        return list
    }

    fun insertData(type: String = PlaceDaoTest.TYPE): List<PlaceDto> {
        val list = buildList(type)
        placeDao.insert(list)
        return list
    }

    @Test
    fun testHasPlaces() {
        insertData()

        assertTrue(placeDaoGeneric.hasPlaces(PlaceTestUtil.defaultRequest()))
    }

    @Test
    fun testGet() {
        insertData()

        val liveData = placeDaoGeneric.get(PlaceTestUtil.defaultRequest())

        liveData.observeOnce { assertEquals(2, it.size) }

        assertEquals("george", liveData.value!!.get(0).name)
        assertEquals("harry", liveData.value!!.get(1).name)
    }

    @Test
    fun testInsert() {
        val list = ArrayList<Place>()
        buildList().forEach { list.add(it.toPlace())}

        placeDaoGeneric.insert(PlaceTestUtil.defaultRequest(), list)

        assertTrue(placeDaoGeneric.hasPlaces(PlaceTestUtil.defaultRequest()))
    }
}