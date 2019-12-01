package com.mwigzell.places.dagger

import android.content.Context
import com.mwigzell.places.TestApplication
import com.mwigzell.places.repository.FileServiceTest
import com.mwigzell.places.repository.api.network.NetworkServiceTest
import com.mwigzell.places.ui.MainActivityTest
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            MockViewModelModule::class,
            TestApplicationModule::class,
            ScreenBindingModule::class,
            AndroidSupportInjectionModule::class
        ]
)
interface TestApplicationComponent : AppComponent {
    fun inject(test: TestApplication)
    fun inject(test: FileServiceTest)
    fun inject(test: MainActivityTest)
    fun inject(test: NetworkServiceTest)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): TestApplicationComponent
    }
}
