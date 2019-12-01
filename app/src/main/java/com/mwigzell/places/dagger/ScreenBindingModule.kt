package com.mwigzell.places.dagger

import com.mwigzell.places.ui.main.MainActivity
import com.mwigzell.places.ui.main.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ScreenBindingModule {
    @ContributesAndroidInjector(modules = [ MainActivityModule::class ])
    abstract fun mainActivity(): MainActivity
}