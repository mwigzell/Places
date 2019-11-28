package com.mwigzell.places.model

import javax.inject.Inject

class PlaceLocation @Inject constructor(latlong: String) {
    val latitude: Double
    val longitude: Double

    init {
        val degrees = latlong.split(',')
        latitude = degrees.get(0).toDouble()
        longitude = degrees.get(1).toDouble()
    }

    constructor(latitude: Double, longitude: Double): this(latitude.toString() + "," + longitude.toString()) {
    }

    override fun toString(): String {
        return latitude.toString() + "," + longitude.toString()
    }
}