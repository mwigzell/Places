package com.mwigzell.places.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.navigation.NavigationView
import com.mwigzell.places.R
import com.mwigzell.places.dagger.ViewModelFactory
import com.mwigzell.places.model.Type
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

    @BindView(R.id.typesContent)
    lateinit internal var types: FrameLayout

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var mainViewModel: MainViewModel
    private var drawerToggle: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)

        drawerToggle = setupDrawerToggle()
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle!!)

        setTypesFragment()
        moveToPlaces()

        mainViewModel = ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]
        mainViewModel.getSelectedType()
                .observe(this, Observer<Type> {
                    title = it.name
                    mDrawer.closeDrawers()
                    mainViewModel.loadPlaces()
                })

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

    private fun setTypesFragment() {
        var typesFragment = TypesFragment()
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.typesContent, typesFragment, "typesFragment")
            .commit()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (drawerToggle!!.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)

    }

    fun moveToPlaces() {
        replaceFragment(PlacesFragment::class.java)
    }
}
