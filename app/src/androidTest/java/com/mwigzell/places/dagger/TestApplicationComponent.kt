package com.mwigzell.places.dagger

import com.mwigzell.places.ActionCreatorAndroidTest
import com.mwigzell.places.DataServiceTest
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [TestApplicationModule::class, ScreenBindingModule::class, AndroidSupportInjectionModule::class])
interface TestApplicationComponent : AppComponent {

    fun inject(test: ActionCreatorAndroidTest)
    fun inject(test: DataServiceTest)
}
