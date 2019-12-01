package com.mwigzell.places.ui

import androidx.lifecycle.ViewModel
import com.mwigzell.places.Mockable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

@Mockable
class BaseViewModel: ViewModel() {
    val trash = CompositeDisposable()

    fun addDisposable(disposable: Disposable) {
        trash.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        trash.clear()
    }
}