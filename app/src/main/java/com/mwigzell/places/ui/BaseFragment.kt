package com.mwigzell.places.ui

import android.Manifest
import android.content.pm.PackageManager
import com.mwigzell.places.repository.LocationService
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject

/**
 *
 */

abstract class BaseFragment : DaggerFragment() {

    @Inject
    lateinit internal var locationService: LocationService

    fun checkResumeLocation() {
        if (!locationService.hasLocationPermissions()) {
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
                locationService.locationResume()
            } else {
                Timber.d("no location permission")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        locationService.locationPause()
    }

    override fun onResume() {
        super.onResume()
        locationService.locationResume()
    }

    companion object {
        private val REQUEST_CODE = 1
    }
}
