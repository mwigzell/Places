package com.mwigzell.places

import android.app.Activity
import android.app.Application
import android.content.Context

import com.crashlytics.android.Crashlytics
import com.mwigzell.places.dagger.AppModule
import com.mwigzell.places.dagger.ScreenBindingModule
import com.mwigzell.places.data.DataService
import com.mwigzell.places.network.NetworkService
import com.mwigzell.places.network.ServiceCreator
import com.mwigzell.places.redux.ActionCreator
import com.mwigzell.places.redux.AppAction
import com.mwigzell.places.redux.AppState
import com.mwigzell.places.redux.jedux.Store
import com.mwigzell.places.redux.original.Subscriber
import com.mwigzell.places.util.Log
import dagger.BindsInstance
import dagger.Component
import dagger.android.DispatchingAndroidInjector

import javax.inject.Inject

import dagger.android.HasActivityInjector
import dagger.android.support.AndroidSupportInjectionModule
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import javax.inject.Singleton

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
    lateinit internal var networkService: NetworkService

    @Inject
    lateinit internal var dataService: DataService

    override fun onCreate() {
        super.onCreate()

        initDagger()

        if (!BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics())
        }
        Log.init(this)
        Timber.d("Hi ho!, its off to work we go!!!")

        /*store!!.subscribe(this)

        if (store!!.state.state() == AppState.States.INIT) {
            actionCreator!!.init()
        } else {
            actionCreator!!.restart()
        }*/
    }

    private fun initDagger() {
        DaggerPlacesApplication_PlacesApplicationComponent.builder()
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

    // Doesn't need to be a nested class. I could also put this in its own file or,
    // this being Kotlin, in the same file but at the top level.
    @Singleton
    @Component(modules = [
        // provided by dagger.android, necessary for injecting framework classes
        AndroidSupportInjectionModule::class,
        AppModule::class,
        // Defines a series of Subcomponents that bind "screens" (Activities, etc)
        ScreenBindingModule::class
    ])
    interface PlacesApplicationComponent {
        fun inject(app: PlacesApplication)

        @Component.Builder
        interface Builder {
            fun build(): PlacesApplicationComponent
            @BindsInstance
            fun app(app: Context): Builder

            fun appModule(module: AppModule): Builder
        }
    }
}
