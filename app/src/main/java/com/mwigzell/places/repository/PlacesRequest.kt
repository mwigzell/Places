package com.mwigzell.places.repository

//TODO: implement "rankBy" with values "prominence" or "distance" etc.
// key used to query Google Places API
// See: https://developers.google.com/places/web-service/search#PlaceSearchRequests
data class PlacesRequest(
        val location: String, // lat long string in decimal degrees e.g. "123.0,456.0"
        val radius: String, // radius in meters
        val type: String) { // type of place to search (canned values
}