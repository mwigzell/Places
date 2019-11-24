package com.mwigzell.places

import com.crashlytics.android.Crashlytics
import com.mwigzell.places.dagger.AppComponent
import com.mwigzell.places.dagger.DaggerAppComponent
import com.mwigzell.places.data.DataService
import com.mwigzell.places.redux.ActionCreator
import com.mwigzell.places.redux.AppAction
import com.mwigzell.places.redux.AppState
import com.mwigzell.places.redux.jedux.Store
import com.mwigzell.places.redux.jedux.Subscriber
import com.mwigzell.places.util.Log
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by mwigzell on 12/10/16.
 */

open class PlacesApplication @Inject constructor(): DaggerApplication(), Subscriber{

    @Inject
    lateinit internal var store: Store<AppAction<Any>, AppState>

    @Inject
    lateinit internal var actionCreator: ActionCreator

    @Inject
    lateinit internal var dataService: DataService

    open lateinit protected var dependencyInjector: AndroidInjector<PlacesApplication>

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

        store.subscribe(this)

        when (store.state.state()) {
            AppState.States.INIT -> actionCreator.init()
            AppState.States.RESTARTED -> actionCreator.init()
            else -> actionCreator.restart()
        }
    }

    override fun onStateChanged() {
        //Timber.d("Got state change");
    }

    companion object {
        val GOOGLE_PLACES_API_KEY = "AIzaSyDYvfBnjsQttjubdoLoyj5IBvf5_x86DtI"
    }
}
