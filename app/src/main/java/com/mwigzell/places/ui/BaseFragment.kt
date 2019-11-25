package com.mwigzell.places.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

import com.mwigzell.places.data.LocationService
import com.mwigzell.places.redux.AppAction
import com.mwigzell.places.redux.AppState
import com.mwigzell.places.redux.jedux.Store
import com.mwigzell.places.redux.jedux.Subscriber
import com.mwigzell.places.redux.jedux.Subscription
import dagger.android.support.DaggerFragment

import javax.inject.Inject

import timber.log.Timber

/**
 * extend LiveData with a function that always removes the observer before subscribing again
 */
fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
    removeObserver(observer)
    observe(owner, observer)
}

/**
 *
 */

abstract class BaseFragment : DaggerFragment(), Subscriber {

    @Inject
    lateinit internal var store: Store<AppAction<Any>, AppState>

    @Inject
    lateinit internal var locationService: LocationService

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
