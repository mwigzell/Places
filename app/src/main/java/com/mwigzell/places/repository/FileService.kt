package com.mwigzell.places.repository

import android.content.Context
import com.mwigzell.places.model.Type
import rx.Observable
import rx.Single
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 *
 */
@Singleton
class DataService @Inject
constructor(private val context: Context) {

    @Throws(Exception::class)
    private fun loadTypes(): List<Type> {
        val result = ArrayList<Type>()
        val reader = BufferedReader(InputStreamReader(context.assets.open("types.txt")))
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
}
