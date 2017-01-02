package com.mwigzell.places.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;

import com.mwigzell.places.data.LocationService;
import com.mwigzell.places.redux.AppAction;
import com.mwigzell.places.redux.AppState;
import com.mwigzell.places.redux.jedux.Store;
import com.mwigzell.places.redux.original.Subscriber;
import com.mwigzell.places.redux.original.Subscription;

import javax.inject.Inject;

import timber.log.Timber;

/**
 *
 */

public abstract class BaseFragment extends Fragment implements Subscriber {
    private static final int REQUEST_CODE = 1;

    @Inject
    Store<AppAction, AppState> store;

    @Inject
    LocationService locationService;

    Subscription subscription;

    public void checkResumeLocation() {
        if (locationService.hasLocationPermissions()) {
            locationService.locationResume();
        } else {
            this.requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Timber.d("onRequestPermissionsResult: requestCode = %d", requestCode);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Start location process
                locationService.locationResume();
            } else {
                Timber.d("no location permission");
            }
        }
    }

    public abstract void onStateChanged();

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume");
        subscription = store.subscribe(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.d("onPause");
        subscription.unsubscribe();
    }
}
