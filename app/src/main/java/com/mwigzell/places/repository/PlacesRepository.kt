package com.mwigzell.places.repository

import androidx.lifecycle.LiveData
import com.mwigzell.places.Mockable
import com.mwigzell.places.model.Place
import com.mwigzell.places.repository.api.network.NetworkService
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Mockable
@Singleton
class PlacesRepository @Inject constructor(
        val networkService: NetworkService,
        val placesDao: PlacesDao
) : Repository() {

    fun loadPlaces(request: PlacesRequest): LiveData<List<Place>> {
        refreshUser(request)
        return placesDao.get(request)
    }

    fun refreshUser(request: PlacesRequest) {
        dispose()

        addDisposable(
                Observable.fromCallable<Boolean>({
                    val exists = placesDao.hasData(request)
                    exists
                })
                .subscribeOn(Schedulers.io())
                .subscribe { exists ->
                    Timber.d("refreshUser: $request exists: $exists")
                    if (!exists) {
                        Timber.d("Not in DB, trying web ...")
                        addDisposable(
                                networkService.getPlaces(request)
                                    .subscribe({
                                        Timber.d("<-- Got places status:" + it.status)
                                        placesDao.insert(request, it.results)
                                    },
                                    { e ->
                                        Timber.e(e)
                                        placesDao.insert(request, ArrayList())
                                    })
                        )
                    } else {
                        Timber.d("Found places in DB!")
                    }
                }
        )
    }
}