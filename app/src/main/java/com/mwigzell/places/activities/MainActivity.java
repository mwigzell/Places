package com.mwigzell.places.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.mwigzell.places.R;
import com.mwigzell.places.dagger.Injection;
import com.mwigzell.places.redux.ActionCreator;
import com.mwigzell.places.redux.AppAction;
import com.mwigzell.places.redux.AppState;
import com.mwigzell.places.redux.jedux.Store;
import com.mwigzell.places.redux.original.Subscriber;
import com.mwigzell.places.redux.original.Subscription;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements Subscriber {
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nvView)
    NavigationView nvDrawer;

    private ActionBarDrawerToggle drawerToggle;

    @Inject
    ActionCreator actionCreator;

    @Inject
    Store<AppAction, AppState> store;

    Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injection.instance().getComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setupDrawerContent(nvDrawer);
        drawerToggle = setupDrawerToggle();
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);
        selectDrawerItem(nvDrawer.getMenu().getItem(0));
        //actionCreator.init();
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    private void replaceFragment(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    private void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = PlacesFragment.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = TypesFragment.class;
                break;
            case R.id.nav_third_fragment:
                fragmentClass = PlacesFragment.class;
                break;
            default:
                fragmentClass = PlacesFragment.class;
        }

        replaceFragment(fragmentClass);

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void moveToPlaces() {
        replaceFragment(PlacesFragment.class);
    }

    public void moveToTypes() {
        replaceFragment(TypesFragment.class);
    }

    @Override
    public void onStateChanged() {
        AppState.States state = store.getState().state();
        //Timber.d("Got state=" + state);
        switch(state) {
            case SELECTED_TYPE:
                moveToPlaces();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        subscription = store.subscribe(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        subscription.unsubscribe();
    }


}
