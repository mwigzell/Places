package com.mwigzell.places.activities

import android.Manifest
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

import com.mwigzell.places.data.LocationService
import com.mwigzell.places.redux.AppAction
import com.mwigzell.places.redux.AppState
import com.mwigzell.places.redux.jedux.Store
import com.mwigzell.places.redux.original.Subscriber
import com.mwigzell.places.redux.original.Subscription

import javax.inject.Inject

import timber.log.Timber

/**
 *
 */

abstract class BaseFragment : Fragment(), Subscriber {

    @Inject
    internal var store: Store<AppAction<Any>, AppState>? = null

    @Inject
    internal var locationService: LocationService? = null

    lateinit internal var subscription: Subscription

    fun checkResumeLocation() {
        if (locationService!!.hasLocationPermissions()) {
            locationService!!.locationResume()
        } else {
            this.requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Timber.d("onRequestPermissionsResult: requestCode = %d", requestCode)
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Start location process
                locationService!!.locationResume()
            } else {
                Timber.d("no location permission")
            }
        }
    }

    abstract override fun onStateChanged()

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
        subscription = store!!.subscribe(this)
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause")
        subscription.unsubscribe()
    }

    companion object {
        private val REQUEST_CODE = 1
    }
}
