package com.mwigzell.places.repository

import com.mwigzell.places.Mockable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

@Mockable
class Repository {
    val trash = CompositeDisposable()

    fun addDisposable(disposable: Disposable) {
        trash.add(disposable)
    }

    fun dispose() {
        trash.clear()
    }
}