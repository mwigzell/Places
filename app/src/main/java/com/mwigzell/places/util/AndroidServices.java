package com.mwigzell.places.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Wrap Android System Service class methods.
 * The main reason for this is so that dependency injection can be implemented, thus allowing
 * unit tests to be written which inject an AndroidServices mock.
 *
 */
@Singleton
public class AndroidServices {

    @Inject
    Context context;

    @Inject
    public AndroidServices() {
    }

    public ConnectivityManager getConnectivityManager() { return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); }

    public boolean isNetwork() {
        ConnectivityManager connectivityManager = getConnectivityManager();
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null) {
            return networkInfo.isConnected();
        }
        return false;
    }
}
