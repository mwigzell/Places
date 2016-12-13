package com.mwigzell.places.util;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.StatFs;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.webkit.WebSettings;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mwigzell.places.Application;
import com.mwigzell.places.BuildConfig;
import com.mwigzell.places.dagger.Injection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

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
        Injection.instance().getComponent().inject(this);
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
