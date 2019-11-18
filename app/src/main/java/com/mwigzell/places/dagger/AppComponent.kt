package com.mwigzell.places.dagger

import android.content.Context
import com.mwigzell.places.PlacesApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

// Doesn't need to be a nested class. I could also put this in its own file or,
// this being Kotlin, in the same file but at the top level.
@Singleton
@Component(
        modules = [
            AndroidSupportInjectionModule::class,
            ViewModelModule::class,
            AppModule::class,
            ScreenBindingModule::class
        ]
)
interface AppComponent {
    fun inject(app: PlacesApplication)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}