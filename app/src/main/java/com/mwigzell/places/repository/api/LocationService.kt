package com.mwigzell.places.repository.api

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.mwigzell.places.Mockable
import com.mwigzell.places.model.PlaceLocation
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import java.text.DecimalFormat
import javax.inject.Inject
import javax.inject.Singleton

@Mockable
@Singleton
class LocationService @Inject
constructor(private val context: Context
) : GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private val dm = DecimalFormat("0.0")

    private var locationClient: GoogleApiClient? = null
    private var locationRequest: LocationRequest? = null
    private var lastLocation: Location? = null
    lateinit private var locationSubject: BehaviorSubject<PlaceLocation>

    init {
        //PlaceLocation client is needed to get at least one location
        if (locationClient == null) {
            locationClient = GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build()
            locationClient!!.connect()
        }
        //PlaceLocation request gets periodic updates on location
        if (locationRequest == null) {
            locationRequest = LocationRequest()
            locationRequest!!.interval = LOCATION_UPDATE_INTERVAL.toLong()
            locationRequest!!.fastestInterval = LOCATION_UPDATE_FASTEST_INTERVAL.toLong()
            locationRequest!!.priority = LocationRequest.PRIORITY_LOW_POWER
        }

        locationSubject = BehaviorSubject.create()
    }

    fun getDefaultLocation(): PlaceLocation {
        return PlaceLocation(DEFAULT_LOCATION)
    }

    fun latLongStrToLocation(str: String): Location {
        val location = Location("LocationService")
        val degrees = str.split(',')
        location.latitude = degrees.get(0).toDouble()
        location.longitude = degrees.get(1).toDouble()
        return location
    }

    fun getLastLocation(): PlaceLocation? {
        lastLocation?.let {
            return PlaceLocation(it.latitude, it.longitude);
        }
        return null
    }

    fun locationResume() {
        Timber.d("locationResume")
        if (locationClient != null && locationClient!!.isConnected) {
            startLocationUpdates()
        }
    }

    fun locationPause() {
        Timber.d("locationPause")
        stopLocationUpdates()
    }


    private fun startLocationUpdates() {
        Timber.d("startLocationUpdates")
        if (hasLocationPermissions()) {
            //If no locations were available, tries to get the last known location
            if (lastLocation == null) {
                updateLastKnownLocation(LocationServices.FusedLocationApi.getLastLocation(locationClient))
            }
            //Starts the location service
            LocationServices.FusedLocationApi.requestLocationUpdates(locationClient, locationRequest, this)

            //When there is no location to check and no PlaceLocation show an error
            if (lastLocation == null && !hasLocationsEnabled())
                Timber.w("no lastLocation and locations not enabled")
        } else {
            Timber.w("no location permission")
            // fallback to a default hard coded location to show something
            locationSubject.onNext(getDefaultLocation())
        }
    }

    private fun stopLocationUpdates() {
        if (locationClient!!.isConnected) {
            LocationServices.FusedLocationApi.removeLocationUpdates(locationClient, this)
        }
    }

    fun observeLocationChanges(): Observable<PlaceLocation> {
        return locationSubject
    }

    override fun onLocationChanged(location: Location) {
        updateLastKnownLocation(location)
    }

    private fun updateLastKnownLocation(location: Location) {
        lastLocation = location
        locationSubject.onNext(PlaceLocation(location.latitude, location.longitude))
    }

    override fun onConnected(connectionHint: Bundle?) {
        if (!hasLocationPermissions()) {
            Timber.d("connected, but no location permission")
            return
        }

        startLocationUpdates()
    }

    override fun onConnectionSuspended(i: Int) {
        Timber.d("connection suspended")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Timber.d("connection failed")
    }

    fun hasLocationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun hasLocationsEnabled(): Boolean {
        // Get GPS and network status
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return isGPSEnabled || isNetworkEnabled
    }

    private fun getItemDistance(location: Location?): String {
        return if (location == null || lastLocation == null) {
            ""
        } else dm.format(getMiles(lastLocation!!.distanceTo(location).toDouble()))

    }

    private fun getMiles(distance: Double): Double {
        return distance * METERS_TO_MILES_CONVERSION
    }

    companion object {
        val LOCATION_UPDATE_INTERVAL = 60 * 1000 //1 minute in millisecons
        val LOCATION_UPDATE_FASTEST_INTERVAL = 30 * 1000 //0.5 minute in milliseconds
        private val METERS_TO_MILES_CONVERSION = 0.000621371192

        val DEFAULT_LOCATION = "-33.8670522,151.1957362"
    }

}
