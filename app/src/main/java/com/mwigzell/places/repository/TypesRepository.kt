package com.mwigzell.places.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mwigzell.places.Mockable
import com.mwigzell.places.model.Type
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Mockable
@Singleton
class TypesRepository @Inject constructor(val fileService: FileService) {
    fun loadTypes(): LiveData<List<Type>> {
        val types: MutableLiveData<List<Type>> = MutableLiveData()
        fileService.fetchTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    Timber.e(it)}
                .subscribe {
                    types.setValue(it)
                }
        return types
    }
}