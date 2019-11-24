package com.mwigzell.places.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.mwigzell.places.R
import com.mwigzell.places.RecyclerViewMatcher
import com.mwigzell.places.TestApplication
import com.mwigzell.places.dagger.TestApplicationComponent
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java, true, true)

    private fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestApplication
        app.getInjector().inject(this)
    }

    @Test
    fun startActivity() {
        val position = 0
        onView(withId(R.id.myList)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position))
        onView(withRecyclerView(R.id.myList).atPositionOnView(position, R.id.name)).check(matches(withText("Sydney")))
    }
}