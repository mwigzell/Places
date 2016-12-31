package com.mwigzell.places.data;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mwigzell.places.dagger.Injection;
import com.mwigzell.places.redux.ActionCreator;
import com.mwigzell.places.redux.AppAction;
import com.mwigzell.places.redux.AppState;
import com.mwigzell.places.redux.original.Store;
import com.mwigzell.places.redux.original.Subscriber;
import com.mwigzell.places.redux.original.Subscription;

import java.text.DecimalFormat;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class LocationService implements Subscriber,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    @Inject
    Store<AppAction, AppState> store;

    @Inject
    Context context;

    @Inject
    ActionCreator actionCreator;

    public static final int LOCATION_UPDATE_INTERVAL = 60 * 1000; //1 minute in millisecons
    public static final int LOCATION_UPDATE_FASTEST_INTERVAL = 30 * 1000; //0.5 minute in milliseconds

    private DecimalFormat dm = new DecimalFormat("0.0");

    private Subscription mSubscription;

    private GoogleApiClient mLocationClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private static final double METERS_TO_MILES_CONVERSION = 0.000621371192;

    @Inject
    public LocationService() {
        Injection.instance().getComponent().inject(this);
        mSubscription = store.subscribe(this);
    }

    @Override
    public void onStateChanged() {
        AppState.States state = store.getState().state;
        Timber.d("onStateChanged: " + state);
        switch(state) {
            case INIT:
                locationCreation();
                break;
        }
    }

    private void locationCreation() {
        //Location client is needed to get at least one location
        if (mLocationClient == null) {
            mLocationClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mLocationClient.connect();
        }
        //Location request gets periodic updates on location
        if (mLocationRequest == null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(LOCATION_UPDATE_INTERVAL);
            mLocationRequest.setFastestInterval(LOCATION_UPDATE_FASTEST_INTERVAL);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
    }

    //public Location getLastLocation() { return mLastLocation; }

    public void locationResume() {
        Timber.d("locationResume");
        if (mLocationClient.isConnected()) {
            startLocationUpdates();
        }
    }

    public void locationPause() {
        stopLocationUpdates();
    }


    @SuppressWarnings({"MissingPermission"})
    private void startLocationUpdates() {
        Timber.d("startLocationUpdates");
        if (hasLocationPermissions()) {
            //If no locations were available, tries to get the last known location
            if (mLastLocation == null) {
                updateLastKnownLocation(LocationServices.FusedLocationApi.getLastLocation(mLocationClient));
            }
            //Starts the location service
            LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);

            //When there is no location to check and no Location show an error
            if (mLastLocation == null && !hasLocationsEnabled())
                Timber.d("no lastLocation and locations not enabled");
        } else {
            Timber.d("no location permission");
        }
    }

    private void stopLocationUpdates() {
        if (mLocationClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mLocationClient, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        updateLastKnownLocation(location);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (!hasLocationPermissions()) {
            Timber.d("connected, but no location permission");
            return;
        }

        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Timber.d("connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.d("connection failed");
    }

    private void updateLastKnownLocation(Location location) {
        mLastLocation = location;
        actionCreator.locationUpdated(location);
    }

    public boolean hasLocationPermissions() {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean hasLocationsEnabled() {
        // Get GPS and network status
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return (isGPSEnabled || isNetworkEnabled);
    }

    private String getItemDistance(Location location) {
        if (location == null || mLastLocation == null) {
            return "";
        }

        return dm.format(getMiles(mLastLocation.distanceTo(location)));
    }

    private double getMiles(double distance) {
        return distance * METERS_TO_MILES_CONVERSION;
    }

}
