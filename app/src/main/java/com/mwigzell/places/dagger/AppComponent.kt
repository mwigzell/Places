package com.mwigzell.places.dagger

import android.content.Context
import com.mwigzell.places.PlacesApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            ViewModelModule::class,
            AppModule::class,
            ScreenBindingModule::class,
            AndroidSupportInjectionModule::class
        ]
)
interface AppComponent: AndroidInjector<PlacesApplication> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}