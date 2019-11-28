package com.mwigzell.places.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.navigation.NavigationView
import com.mwigzell.places.R
import dagger.Module
import dagger.android.AndroidInjection
import dagger.android.ContributesAndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

// We can define this anywhere we like, but it's convenient to include
// in the same file as the class being injected
// An object because I want to provide a static function, and it's Kotlin
@Module abstract class MainActivityModule {
    @ContributesAndroidInjector abstract fun typesFragment(): TypesFragment
    @ContributesAndroidInjector abstract fun placesFragment(): PlacesFragment
}

class MainActivity : DaggerAppCompatActivity() {
    @BindView(R.id.drawer_layout)
    lateinit internal var mDrawer: DrawerLayout

    @BindView(R.id.toolbar)
    lateinit internal var toolbar: Toolbar

    @BindView(R.id.nvView)
    lateinit internal var nvDrawer: NavigationView

    private var drawerToggle: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        setupDrawerContent(nvDrawer)
        drawerToggle = setupDrawerToggle()
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle!!)
        selectDrawerItem(nvDrawer.menu.getItem(0))
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
        var fragment: Fragment
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
        mDrawer.closeDrawers()
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
}
