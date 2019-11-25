package com.mwigzell.places

import com.crashlytics.android.Crashlytics
import com.mwigzell.places.dagger.DaggerAppComponent
import com.mwigzell.places.util.Log
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import javax.inject.Inject

//TODO: Fix mocking ViewModels
//TODO: Remove & Replace ActionCreator actions
//TODO: Redo UI so it works, use types as paging tabbed header, also as choice list
//TODO: Introduce Jetpack Navigator
//TODO: Architect the Repository abstraction behind ViewModels
//TODO: Run memory analyzer

/**
 * Created by mwigzell on 12/10/16.
 */
open class PlacesApplication @Inject constructor(): DaggerApplication() {

    lateinit protected var dependencyInjector: AndroidInjector<PlacesApplication>

    open protected fun createInjector(): AndroidInjector<PlacesApplication> {
        return DaggerAppComponent
                .factory()
                .create(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        if (! ::dependencyInjector.isInitialized) {
            dependencyInjector = createInjector()
        }
        return dependencyInjector
    }

    override fun onCreate() {
        super.onCreate()

        Thread.setDefaultUncaughtExceptionHandler(object : Thread.UncaughtExceptionHandler {
            override fun uncaughtException(thread: Thread, ex: Throwable) {
                ex.printStackTrace()
                System.exit(0)
            }
        })

        if (!BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics())
        }
        Log.init(this)
        Timber.d("Hi ho!, its off to work we go!!!")
    }

    companion object {
        val GOOGLE_PLACES_API_KEY = "AIzaSyDYvfBnjsQttjubdoLoyj5IBvf5_x86DtI"
    }
}
