package com.mwigzell.places

import android.content.SharedPreferences

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mwigzell.places.dagger.TestApplicationComponent

import com.mwigzell.places.data.DataService
import com.mwigzell.places.redux.ActionCreator
import com.mwigzell.places.redux.AppAction
import com.mwigzell.places.redux.AppState
import com.mwigzell.places.redux.jedux.Store
import com.mwigzell.places.util.TestApplication

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import javax.inject.Inject

import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue

/**
 *
 */
@RunWith(AndroidJUnit4::class)
class DataServiceTest {
    @Inject
    lateinit internal var store: Store<AppAction<Any>, AppState>

    lateinit internal var dataService: DataService
    lateinit internal var preferences: SharedPreferences

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val app = context.applicationContext as TestApplication
        (app.getAppComponent() as TestApplicationComponent).inject(this)


        preferences = context.getSharedPreferences("data", 0)
        preferences.edit().remove("data").commit()

        val actionCreator = ActionCreator(store)
        dataService = DataService(context, actionCreator, store!!)
    }

    @After
    fun teardown() {
        preferences.edit().remove("data").commit()
    }

    @Test
    @Throws(Exception::class)
    fun loadTypes() {
        val types = dataService.loadTypes()

        assertNotNull(types)
        assertTrue(types.size > 0)
    }
}