package com.mwigzell.places.dagger

import android.content.Context
import com.mwigzell.places.PlacesApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

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
interface AppComponent: AndroidInjector<DaggerApplication> {
    fun inject(app: PlacesApplication)
}