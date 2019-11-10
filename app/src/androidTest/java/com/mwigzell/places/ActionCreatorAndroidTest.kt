package com.mwigzell.places

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import com.mwigzell.places.dagger.AppModule
import com.mwigzell.places.redux.ActionCreator
import com.mwigzell.places.redux.AppAction
import com.mwigzell.places.redux.AppState
import com.mwigzell.places.redux.jedux.Store
import com.mwigzell.places.redux.jedux.Subscriber

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.Mockito.*

import javax.inject.Inject

import org.junit.Assert.assertEquals

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ActionCreatorAndroidTest {
    lateinit internal var subscriber: Subscriber

    @Inject
    lateinit internal var store: Store<AppAction<Any>, AppState>

    lateinit internal var actionCreator: ActionCreator

    @Before
    fun setup() {
        val appModule = AppModule(InstrumentationRegistry.getInstrumentation().targetContext)
        store = appModule.provideStore()

        subscriber = mock(Subscriber::class.java)
        actionCreator = ActionCreator(store)
    }

    @Test
    @Throws(Exception::class)
    fun testInit() {
        store!!.subscribe(subscriber)
        actionCreator.init()

        assertEquals(AppState.States.INIT, store!!.state.state())
        verify(subscriber).onStateChanged()
    }
}