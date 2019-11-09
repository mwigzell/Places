package com.mwigzell.places.dagger

import android.content.Context

import com.mwigzell.places.PlacesApplication
import com.mwigzell.places.activities.MainActivity
import com.mwigzell.places.activities.PlacesFragment
import com.mwigzell.places.activities.TypesFragment
import com.mwigzell.places.activities.TypesViewAdapter
import com.mwigzell.places.data.DataService
import com.mwigzell.places.data.LocationService
import com.mwigzell.places.network.NetworkService
import com.mwigzell.places.network.ServiceCreator
import com.mwigzell.places.util.AndroidServices
import com.mwigzell.places.util.FileUtils
import dagger.BindsInstance

import javax.inject.Singleton

import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

/**
 * Provides common base of inject() signatures that serve both the runtime and mock
 * frameworks.
 */

/*@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(PlacesApplication app);
    void inject(MainActivity activity);
    void inject(ServiceCreator creator);
    void inject(FileUtils utils);
    void inject(AndroidServices services);
    void inject(NetworkService service);
    void inject(PlacesFragment places);
    void inject(TypesFragment types);
    void inject(DataService service);
    void inject(LocationService service);
    void inject(TypesViewAdapter adapter);

    Context context();
}*/

// Doesn't need to be a nested class. I could also put this in its own file or,
// this being Kotlin, in the same file but at the top level.
/*
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
    }
}*/
