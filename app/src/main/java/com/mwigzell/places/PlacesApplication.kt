package com.mwigzell.places

import android.app.Activity
import android.app.Application
import com.crashlytics.android.Crashlytics
import com.mwigzell.places.dagger.AppModule
import com.mwigzell.places.dagger.DaggerPlacesApplicationComponent
import com.mwigzell.places.data.DataService
import com.mwigzell.places.redux.ActionCreator
import com.mwigzell.places.redux.AppAction
import com.mwigzell.places.redux.AppState
import com.mwigzell.places.redux.jedux.Store
import com.mwigzell.places.redux.jedux.Subscriber
import com.mwigzell.places.util.Log
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by mwigzell on 12/10/16.
 */

open class PlacesApplication : Application(), Subscriber, HasActivityInjector {
    // Required by HasActivityInjector, and injected below
    @Inject
    protected lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    override fun activityInjector() = dispatchingAndroidInjector

    @Inject
    lateinit internal var store: Store<AppAction<Any>, AppState>

    @Inject
    lateinit internal var actionCreator: ActionCreator

    @Inject
    lateinit internal var dataService: DataService

    override fun onCreate() {
        super.onCreate()

        Thread.setDefaultUncaughtExceptionHandler(object : Thread.UncaughtExceptionHandler {
            override fun uncaughtException(thread: Thread, ex: Throwable) {
                ex.printStackTrace()
                System.exit(0)
            }
        })

        initDagger()

        if (!BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics())
        }
        Log.init(this)
        Timber.d("Hi ho!, its off to work we go!!!")

        store!!.subscribe(this)

        when (store!!.state.state()) {
            AppState.States.INIT -> actionCreator!!.init()
            AppState.States.RESTARTED -> actionCreator!!.init()
            else -> actionCreator!!.restart()
        }
    }

    private fun initDagger() {
        DaggerPlacesApplicationComponent.builder()
                .app(this)
                .appModule(AppModule(this))
                .build()
                .inject(this)
    }

    override fun onStateChanged() {
        //Timber.d("Got state change");
    }

    companion object {
        val GOOGLE_PLACES_API_KEY = "AIzaSyDYvfBnjsQttjubdoLoyj5IBvf5_x86DtI"
    }
}
