package com.mwigzell.places.ui

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.mwigzell.places.RecyclerViewMatcher
import com.mwigzell.places.TestApplication
import com.mwigzell.places.dagger.MockViewModelModule
import com.mwigzell.places.model.Place
import com.mwigzell.places.model.Type
import com.mwigzell.places.ui.main.MainActivity
import com.mwigzell.places.ui.main.MainViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


class MainActivityTest {
    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java, true, false)

    lateinit var mainViewModel: MainViewModel
    val places: MutableLiveData<List<Place>> = MutableLiveData()
    val selectedType: MutableLiveData<Type> = MutableLiveData()
    val types: MutableLiveData<List<Type>> = MutableLiveData()

    private fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestApplication
        app.getInjector().inject(this)
        mainViewModel = MockViewModelModule.mainViewModel

        `when`(mainViewModel.getPlaces()).thenReturn(places)
        `when`(mainViewModel.getSelectedType()).thenReturn(selectedType)
        `when`(mainViewModel.getTypes()).thenReturn(types)
        activityRule.launchActivity(Intent())
    }

    @Test
    fun placesShown() {
        val placeList: ArrayList<Place> = ArrayList()
        val place = Place()
        val name = "Sydney"
        place.name = name
        val photos = ArrayList<Place.Photo>()
        val photo = place.Photo()
        photo.photoReference = "CmRaAAAA6U_i750l19d37AbcRN5JPw4JYx0uAlbESXoyNAQp6FU4szYPzwojN7x88Zs2etOQTK8QtFBZdVtki0WRggNONoHpSyr9cy4rslFNfkMkExgUCUHCslgt0YMHa0P1K3-4EhBMrLrKEbpobNmo9U0l3YrwGhTZNzjceIxjy2o3ZdLxhHkVRnEezw"
        photos.add(photo)
        place.photos = photos
        placeList.add(place)
        activityRule.activity.runOnUiThread {
            places.value = placeList
        }

        val position = 0
        onView(withId(com.mwigzell.places.R.id.placeList)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position))
        onView(withRecyclerView(com.mwigzell.places.R.id.placeList).atPositionOnView(position, com.mwigzell.places.R.id.name)).check(matches(withText(name)))
    }

    @Test
    fun selectedType() {
        val type = "bakery"
        activityRule.activity.runOnUiThread {
            selectedType.value = Type(type)
        }

        onView(withText(type)).check(matches(isDisplayed()))
    }
}