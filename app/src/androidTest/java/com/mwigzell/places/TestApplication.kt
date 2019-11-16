package com.mwigzell.places

import androidx.fragment.app.Fragment
import com.mwigzell.places.PlacesApplication
import com.mwigzell.places.dagger.*
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class TestApplication : PlacesApplication(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = fragmentInjector

    override fun createDaggerComponent() {
        dependencyInjector = DaggerTestApplicationComponent.builder()
                .testApplicationModule(TestApplicationModule(this))
                .build()
        dependencyInjector.inject(this)
    }

    fun setAppComponent(dependencyInjector : TestApplicationComponent) {
        this.dependencyInjector = dependencyInjector
    }
}