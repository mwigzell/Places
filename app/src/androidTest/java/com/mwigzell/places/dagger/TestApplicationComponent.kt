package com.mwigzell.places.dagger

import android.content.Context
import com.mwigzell.places.TestApplication
import com.mwigzell.places.data.DataServiceTest
import com.mwigzell.places.ui.MainActivityTest
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            ViewModelModule::class,
            TestApplicationModule::class,
            ScreenBindingModule::class,
            AndroidSupportInjectionModule::class
        ]
)
interface TestApplicationComponent : AppComponent {
    fun inject(test: TestApplication)
    fun inject(test: DataServiceTest)
    fun inject(test: MainActivityTest)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): TestApplicationComponent
    }
}
