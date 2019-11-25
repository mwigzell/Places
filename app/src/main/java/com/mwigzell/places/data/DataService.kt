package com.mwigzell.places.data


import android.content.Context
import android.os.AsyncTask

import com.mwigzell.places.model.Type
import com.mwigzell.places.redux.ActionCreator
import com.mwigzell.places.redux.AppAction
import com.mwigzell.places.redux.AppState
import com.mwigzell.places.redux.jedux.Store
import com.mwigzell.places.redux.jedux.Subscriber
import rx.Observable
import rx.Single
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.ArrayList

import javax.inject.Inject
import javax.inject.Singleton

/**
 *
 */
@Singleton
class DataService @Inject
constructor(private val context: Context,
            private val actionCreator: ActionCreator,
            private val store: Store<AppAction<Any>, AppState>) : Subscriber {
    lateinit internal var lastError: Exception

    init {
        store.subscribe(this)
    }

    @Throws(Exception::class)
    fun loadTypes(): List<Type> {
        val result = ArrayList<Type>()
        val reader = BufferedReader(InputStreamReader(context!!.assets.open("types.txt")))
        while (true) {
            val line = reader.readLine() ?: break
            result.add(Type(line))
        }
        return result
    }

    fun fetchTypes(): Observable<List<Type>> {
        return Single.fromCallable {
            loadTypes()
        }.toObservable()
    }

    override fun onStateChanged() {
        //Timber.d("got state: " + store.getState().state);
        when (store.state.state()) {
            AppState.States.LOAD_TYPES -> {
                val types = store.state.types()
                if (types.size == 0) {
                    fetchTypes()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnError {
                                Timber.e(it)}
                            .subscribe {
                                actionCreator.typesLoaded(it)
                            }

                } else {
                    actionCreator.typesLoaded(types)
                }
            }
        }
    }
}
