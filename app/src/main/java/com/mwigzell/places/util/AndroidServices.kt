package com.mwigzell.places.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.mwigzell.places.repository.api.network.NetworkStatus
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Wrap Android System Service class methods.
 * The main reason for this is so that dependency injection can be implemented, thus allowing
 * unit tests to be written which inject an AndroidServices mock.
 *
 */
@Singleton
class AndroidServices @Inject
constructor(private val context: Context): NetworkStatus {
    val connectivityManager: ConnectivityManager
        get() = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override val isNetwork: Boolean
        get() {
            val connectivityManager = connectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo?.isConnected ?: false
        }
}
