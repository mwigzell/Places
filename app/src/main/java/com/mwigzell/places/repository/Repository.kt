package com.mwigzell.places.repository

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class Repository {
    val trash = CompositeDisposable()

    fun addDisposable(disposable: Disposable) {
        trash.add(disposable)
    }

    fun clear() {
        trash.clear()
    }
}