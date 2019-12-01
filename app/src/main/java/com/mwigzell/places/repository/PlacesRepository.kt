package com.mwigzell.places.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mwigzell.places.Mockable
import com.mwigzell.places.model.Place
import com.mwigzell.places.repository.api.network.NetworkService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Mockable
@Singleton
class PlacesRepository @Inject constructor(val networkService: NetworkService): Repository() {

    fun loadPlaces(location: String, radius:String, name: String): LiveData<List<Place>> {
        val places: MutableLiveData<List<Place>> = MutableLiveData()
        dispose()
        addDisposable(networkService.getPlaces(location, radius, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    Timber.e(it, "<--  error with exception")
                    places.setValue(ArrayList())
                }
                .subscribe {
                    Timber.d("<-- Got places status:" + it.status)
                    places.setValue(it.results)
                })
        return places
    }
}