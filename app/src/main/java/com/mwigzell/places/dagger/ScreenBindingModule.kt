package com.mwigzell.places.dagger

import com.mwigzell.places.activities.MainActivity
import com.mwigzell.places.activities.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ScreenBindingModule {
    @ContributesAndroidInjector(modules = [ MainActivityModule::class ])
    abstract fun mainActivity(): MainActivity
}