package com.mwigzell.places.data

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
import com.mwigzell.places.model.PlaceLocation
import rx.Observable
import rx.subjects.BehaviorSubject
import timber.log.Timber
import java.text.DecimalFormat
import javax.inject.Inject
import javax.inject.Singleton

//TODO: pause and resume location updates according to life cycle and location permission
//TODO: persist last known location

@Singleton
class LocationService @Inject
constructor(private val context: Context
) : GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private val dm = DecimalFormat("0.0")

    private var mLocationClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLastLocation: Location? = null
    lateinit private var locationSubject: BehaviorSubject<PlaceLocation>

    init {
        initService()
    }

    private fun initService() {
        //PlaceLocation client is needed to get at least one location
        if (mLocationClient == null) {
            mLocationClient = GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build()
            mLocationClient!!.connect()
        }
        //PlaceLocation request gets periodic updates on location
        if (mLocationRequest == null) {
            mLocationRequest = LocationRequest()
            mLocationRequest!!.interval = LOCATION_UPDATE_INTERVAL.toLong()
            mLocationRequest!!.fastestInterval = LOCATION_UPDATE_FASTEST_INTERVAL.toLong()
            mLocationRequest!!.priority = LocationRequest.PRIORITY_LOW_POWER
        }

        if (mLastLocation != null) {
            locationSubject = BehaviorSubject.create(PlaceLocation(mLastLocation!!.latitude, mLastLocation!!.longitude))
        } else {
            locationSubject = BehaviorSubject.create(getDefaultLocation())
        }
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

    //public PlaceLocation getLastLocation() { return mLastLocation; }

    fun locationResume() {
        Timber.d("locationResume")
        if (mLocationClient != null && mLocationClient!!.isConnected) {
            startLocationUpdates()
        }
    }

    fun locationPause() {
        stopLocationUpdates()
    }


    private fun startLocationUpdates() {
        Timber.d("startLocationUpdates")
        if (hasLocationPermissions()) {
            //If no locations were available, tries to get the last known location
            if (mLastLocation == null) {
                updateLastKnownLocation(LocationServices.FusedLocationApi.getLastLocation(mLocationClient))
            }
            //Starts the location service
            LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this)

            //When there is no location to check and no PlaceLocation show an error
            if (mLastLocation == null && !hasLocationsEnabled())
                Timber.d("no lastLocation and locations not enabled")
        } else {
            Timber.d("no location permission")
        }
    }

    private fun stopLocationUpdates() {
        if (mLocationClient!!.isConnected) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mLocationClient, this)
        }
    }

    fun observeLocationChanges(): Observable<PlaceLocation> {
        return locationSubject
    }

    override fun onLocationChanged(location: Location) {
        updateLastKnownLocation(location)
    }

    private fun updateLastKnownLocation(location: Location) {
        mLastLocation = location
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
        return if (location == null || mLastLocation == null) {
            ""
        } else dm.format(getMiles(mLastLocation!!.distanceTo(location).toDouble()))

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
