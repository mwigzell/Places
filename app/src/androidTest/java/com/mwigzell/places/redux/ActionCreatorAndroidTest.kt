package com.mwigzell.places.redux

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.mwigzell.places.TestApplication
import com.mwigzell.places.dagger.TestApplicationComponent
import com.mwigzell.places.redux.jedux.Store
import com.mwigzell.places.redux.jedux.Subscriber
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

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
        subscriber = mock(Subscriber::class.java)

        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestApplication
        app.getInjector().inject(this)

        actionCreator = ActionCreator(store)
    }

    @Test
    @Throws(Exception::class)
    fun testInit() {
        store.subscribe(subscriber)
        actionCreator.init()

        assertEquals(AppState.States.INIT, store.state.state())
        verify(subscriber).onStateChanged()
    }
}