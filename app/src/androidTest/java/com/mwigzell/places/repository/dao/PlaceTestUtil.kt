package com.mwigzell.places.repository.dao

import com.mwigzell.places.repository.PlacesRequest

class PlaceTestUtil {

    companion object {
        fun create(name: String, photoReference: String, type: String = PlaceDaoTest.TYPE): PlaceDto {
            return PlaceDto(PlaceKey(PlaceDaoTest.LATLONG, PlaceDaoTest.RADIUS, type, name), photoReference)
        }

        fun defaultRequest(): PlacesRequest {
            return PlacesRequest(LATLONG, RADIUS, TYPE)
        }

        const val LATLONG = "123.0,456.0"
        const val RADIUS = "500"
        const val TYPE = "bakery"
        const val TYPE2 = "hairdresser"
    }
}