package com.mwigzell.places.dagger

import com.mwigzell.places.PlacesApplication

class DebugPlacesApplication : PlacesApplication() {
    fun setTestComponent(component: PlacesApplicationComponent) {
        component.inject(this)
    }
}