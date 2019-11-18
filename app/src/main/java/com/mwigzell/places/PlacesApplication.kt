package com.mwigzell.places

import android.app.Application
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
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by mwigzell on 12/10/16.
 */

open class PlacesApplication @Inject constructor(): Application(), Subscriber, HasAndroidInjector {

    @Inject
    lateinit internal var store: Store<AppAction<Any>, AppState>

    @Inject
    lateinit internal var actionCreator: ActionCreator

    @Inject
    lateinit internal var dataService: DataService

    open lateinit protected var dependencyInjector: AppComponent

    // Required by HasActivityInjector, and injected below
    @Inject
    protected lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>
    override fun androidInjector() = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()

        createDaggerComponent()

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

    open protected fun createDaggerComponent() {
        dependencyInjector = DaggerAppComponent.factory()
                .create(this)
        dependencyInjector.inject(this)
    }

    fun getAppComponent(): AppComponent {
        return dependencyInjector
    }

    override fun onStateChanged() {
        //Timber.d("Got state change");
    }

    companion object {
        val GOOGLE_PLACES_API_KEY = "AIzaSyDYvfBnjsQttjubdoLoyj5IBvf5_x86DtI"
    }
}
