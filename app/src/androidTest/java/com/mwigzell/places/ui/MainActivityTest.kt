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
    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java, true, false)

    lateinit var mainViewModel: MainViewModel
    val places: MutableLiveData<List<Place>> = MutableLiveData()

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
        val position = 0
        onView(withId(R.id.myList)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position))
        val placeList: ArrayList<Place> = ArrayList()
        val place = Place()
        val name = "Sydney"
        place.name = name
        val photos = ArrayList<Place.Photo>()
        val photo = place.Photo()
        photo.photoReference = "CmRaAAAA6U_i750l19d37AbcRN5JPw4JYx0uAlbESXoyNAQp6FU4szYPzwojN7x88Zs2etOQTK8QtFBZdVtki0WRggNONoHpSyr9cy4rslFNfkMkExgUCUHCslgt0YMHa0P1K3-4EhBMrLrKEbpobNmo9U0l3YrwGhTZNzjceIxjy2o3ZdLxhHkVRnEezw"
        photos.add(photo)
        placeList.add(place)
        activityRule.activity.runOnUiThread {
            places.value = placeList
        }

        onView(withRecyclerView(R.id.myList).atPositionOnView(position, R.id.name)).check(matches(withText(name)))
    }
}