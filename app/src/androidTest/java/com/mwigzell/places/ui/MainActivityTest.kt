package com.mwigzell.places.ui

import android.content.Intent
import androidx.lifecycle.LiveData
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
import com.mwigzell.places.AsyncTaskSchedulerRule
import com.mwigzell.places.R
import com.mwigzell.places.RecyclerViewMatcher
import com.mwigzell.places.TestApplication
import com.mwigzell.places.dagger.MockViewModelModule
import com.mwigzell.places.dagger.TestApplicationComponent
import com.mwigzell.places.dagger.ViewModelFactory
import com.mwigzell.places.model.Place
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    //@Rule val async = AsyncTaskSchedulerRule()

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java, true, false)

    lateinit var mainViewModel: MainViewModel
    val places: LiveData<List<Place>> = MutableLiveData()

    private fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }

    @Before
    fun setUp() {
        //MockitoAnnotations.initMocks(this)

        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestApplication
        app.getInjector().inject(this)
        mainViewModel = MockViewModelModule.mainViewModel

        `when`(mainViewModel.getPlaces()).thenReturn(places)
        activityRule.launchActivity(Intent())
    }

    @Test
    fun startActivity() {
        //`when`(mainViewModel.getPlaces()).thenReturn(MutableLiveData())
        val position = 0
        onView(withId(R.id.myList)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position))
        onView(withRecyclerView(R.id.myList).atPositionOnView(position, R.id.name)).check(matches(withText("Sydney")))
    }
}