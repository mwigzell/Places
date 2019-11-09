package com.mwigzell.places.activities

import android.content.res.Configuration
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.fragment.app.Fragment
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem

import com.mwigzell.places.R
import com.mwigzell.places.redux.ActionCreator
import com.mwigzell.places.redux.AppAction
import com.mwigzell.places.redux.AppState
import com.mwigzell.places.redux.jedux.Store
import com.mwigzell.places.redux.original.Subscriber
import com.mwigzell.places.redux.original.Subscription

import javax.inject.Inject

import butterknife.BindView
import butterknife.ButterKnife
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjection

// We can define this anywhere we like, but it's convenient to include
// in the same file as the class being injected
// An object because I want to provide a static function, and it's Kotlin
@Module
object MainActivityModule {
    // static because dagger can call this method like MainActivityModule.provideText(),
    // rather than new MainActivityModule().provideText()
    @Provides
    @JvmStatic fun provideText() = "Why, hello there!"
}

class MainActivity : AppCompatActivity(), Subscriber {
    @BindView(R.id.drawer_layout)
    lateinit internal var mDrawer: DrawerLayout

    @BindView(R.id.toolbar)
    lateinit internal var toolbar: Toolbar

    @BindView(R.id.nvView)
    lateinit internal var nvDrawer: NavigationView

    private var drawerToggle: ActionBarDrawerToggle? = null

    @Inject
    lateinit internal var actionCreator: ActionCreator

    @Inject
    lateinit internal var store: Store<AppAction<Any>, AppState>

    lateinit internal var subscription: Subscription

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        setupDrawerContent(nvDrawer!!)
        drawerToggle = setupDrawerToggle()
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer!!.addDrawerListener(drawerToggle!!)
        selectDrawerItem(nvDrawer!!.menu.getItem(0))
        //actionCreator.init();
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle!!.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Pass any configuration change to the drawer toggles
        drawerToggle!!.onConfigurationChanged(newConfig)
    }

    private fun setupDrawerToggle(): ActionBarDrawerToggle {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close)
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            selectDrawerItem(menuItem)
            true
        }
    }

    private fun replaceFragment(fragmentClass: Class<*>) {
        var fragment: Fragment? = null
        try {
            fragment = fragmentClass.newInstance() as Fragment
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }

        // Insert the fragment by replacing any existing fragment
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit()
    }

    private fun selectDrawerItem(menuItem: MenuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        val fragment: Fragment? = null
        val fragmentClass: Class<*>
        when (menuItem.itemId) {
            R.id.nav_first_fragment -> fragmentClass = PlacesFragment::class.java
            R.id.nav_second_fragment -> fragmentClass = TypesFragment::class.java
            R.id.nav_third_fragment -> fragmentClass = PlacesFragment::class.java
            else -> fragmentClass = PlacesFragment::class.java
        }

        replaceFragment(fragmentClass)

        // Highlight the selected item has been done by NavigationView
        menuItem.isChecked = true
        // Set action bar title
        title = menuItem.title
        // Close the navigation drawer
        mDrawer!!.closeDrawers()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (drawerToggle!!.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)

    }

    fun moveToPlaces() {
        replaceFragment(PlacesFragment::class.java)
    }

    fun moveToTypes() {
        replaceFragment(TypesFragment::class.java)
    }

    override fun onStateChanged() {
        val state = store!!.state.state()
        //Timber.d("Got state=" + state);
        when (state) {
            AppState.States.SELECTED_TYPE -> moveToPlaces()
        }
    }

    override fun onResume() {
        super.onResume()

        subscription = store!!.subscribe(this)
    }

    override fun onPause() {
        super.onPause()

        subscription.unsubscribe()
    }


}
