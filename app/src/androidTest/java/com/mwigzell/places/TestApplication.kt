package com.mwigzell.places

import com.mwigzell.places.dagger.DaggerTestApplicationComponent
import com.mwigzell.places.dagger.TestApplicationComponent

class TestApplication: PlacesApplication() {

    override fun createInjector(): TestApplicationComponent {
        return DaggerTestApplicationComponent
                    .factory()
                    .create(this)
    }

    fun getInjector(): TestApplicationComponent {
        return dependencyInjector as TestApplicationComponent
    }
}