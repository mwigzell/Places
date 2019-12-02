package com.mwigzell.places.repository

data class PlacesRequest(
        val location: String,
        val radius: String,
        val type: String) {

    /*override fun toString(): String {
        "loc=$location radius=$radius type=$type"
    }*/
}