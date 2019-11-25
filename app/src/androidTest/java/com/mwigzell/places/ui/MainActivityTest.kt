package com.mwigzell.places.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
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
import com.mwigzell.places.dagger.ViewModelFactory
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java, true, true)

    //@Inject
    //lateinit var viewModelFactory: ViewModelFactory

    //lateinit var mainViewModel: MainViewModel

    private fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestApplication
        app.getInjector().inject(this)
        //mainViewModel = ViewModelProviders.of(activityRule.activity, viewModelFactory)[MainViewModel::class.java]
    }

    @Test
    fun startActivity() {
        //`when`(mainViewModel.getPlaces()).thenReturn(MutableLiveData())
        val position = 0
        onView(withId(R.id.myList)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position))
        onView(withRecyclerView(R.id.myList).atPositionOnView(position, R.id.name)).check(matches(withText("Sydney")))
    }
}