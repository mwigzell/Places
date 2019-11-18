package com.mwigzell.places

import com.mwigzell.places.dagger.DaggerTestApplicationComponent
import com.mwigzell.places.dagger.TestApplicationComponent
import com.mwigzell.places.dagger.TestApplicationModule


class TestApplication : PlacesApplication() {

    override fun createDaggerComponent() {
        super.dependencyInjector = DaggerTestApplicationComponent.factory().create(this, TestApplicationModule())
        super.dependencyInjector.inject(this)
    }

    fun setAppComponent(dependencyInjector : TestApplicationComponent) {
        super.dependencyInjector = dependencyInjector
    }
}