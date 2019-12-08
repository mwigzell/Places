package com.mwigzell.places.repository

import androidx.lifecycle.LiveData
import com.mwigzell.places.Mockable
import com.mwigzell.places.model.Place
import com.mwigzell.places.repository.api.PlacesResponse
import com.mwigzell.places.repository.api.network.NetworkService
import com.mwigzell.places.repository.dao.IPlaceDaoGeneric
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Mockable
@Singleton
class PlacesRepository @Inject constructor(
        val networkService: NetworkService,
        val IPlaceDaoGeneric: IPlaceDaoGeneric
) : Repository() {

    fun loadPlaces(request: PlacesRequest): LiveData<List<Place>> {
        refreshUser(request)
        return IPlaceDaoGeneric.get(request)
    }

    private fun refreshUser(request: PlacesRequest) {
        dispose()

        val observable: Observable<PlacesResponse> =  Observable.create( { emitter ->
            val exists = IPlaceDaoGeneric.hasPlaces(request)

            Timber.d("refreshUser: $request exists: $exists")
            if (!exists) {
                Timber.d("Not in DB, trying web ...")
                networkService.getPlaces(request)
                        .subscribe(
                                {
                                    placesResponse -> Timber.d("Succeeded: $placesResponse")
                                    emitter.onNext(placesResponse)
                                    emitter.onComplete()
                                },
                                {
                                    e -> Timber.e(e)
                                    emitter.onError(e)
                                },
                                {
                                    Timber.d("Completed getPlaces()")
                                    emitter.onComplete()
                                }
                        )

            } else {
                Timber.d("Found places in DB!")
                emitter.onComplete()
            }

        })

        val disposable = observable
                .subscribeOn(Schedulers.io())
                .map { response:PlacesResponse ->
                    when(response.status) {
                        "OK" -> insertPlaces(request, response.results).subscribe()
                        else -> insertPlaces(request, ArrayList()).subscribe()
                    }
                    true
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {d -> Timber.d("done: d: $d")},
                        {e -> Timber.e(e)},
                        {Timber.d("Completed loadPlaces()")}
                )
        addDisposable(disposable)

    }

    private fun insertPlaces(request: PlacesRequest, places: List<Place>): Completable {
        return Completable.fromCallable {
            IPlaceDaoGeneric.insert(request, places)
            Timber.d("did IPlaceDaoGeneric.insert()")
        }
    }
}