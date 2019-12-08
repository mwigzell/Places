package com.mwigzell.places.repository.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mwigzell.places.model.Place
import com.mwigzell.places.repository.PlacesRequest
import timber.log.Timber
import javax.inject.Inject

/**
 * Mediate between abstract model and dto for ROOM implementation
 */
class PlaceDaoGeneric @Inject constructor(db: PlacesDatabase) : IPlaceDaoGeneric {
    val placesDao: PlaceDao

    init {
        placesDao = db.getPlaceDao()
    }

    override fun hasPlaces(request: PlacesRequest): Boolean {
        Timber.d("called")
        return placesDao.hasPlaces(request.location, request.radius, request.type) > 0
    }

    override fun get(request: PlacesRequest): LiveData<List<Place>> {
        Timber.d("called")
        val liveData = placesDao.get(request.location, request.radius, request.type)
        return Transformations.switchMap(liveData) {
            val list = ArrayList<Place>()
            it.forEach { list.add(it.toPlace()) }
            val liveData: MutableLiveData<List<Place>> = MutableLiveData()
            liveData.value = list
            liveData
        }
    }

    override fun insert(request: PlacesRequest, places: List<Place>) {
        Timber.d("called")
        val list = ArrayList<PlaceDto>()
        places.forEach {
            val placeDto = fromPlace(request, it)
            Timber.d("$placeDto")
            list.add(placeDto)}
        placesDao.insert(list)
    }

    private fun fromPlace(request: PlacesRequest, place: Place): PlaceDto {
        var photoReference = ""
        place.photos?.let {
            if ( it.size > 0) {
                val photo = it.get(0)
                photoReference.let { photoReference = photo.photoReference }
            }
        }
        return PlaceDto(PlaceKey(
                request.location,
                request.radius,
                request.type,
                place.name),
                photoReference)
    }
}