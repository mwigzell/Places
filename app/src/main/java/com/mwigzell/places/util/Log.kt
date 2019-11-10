package com.mwigzell.places.util

import android.content.Context

import com.mwigzell.places.BuildConfig
import com.mwigzell.places.R

import java.util.Locale

import timber.log.Timber


/**
 * Created by mwigzell on 12/10/16.
 */

object Log {
    private val PREFACE_LENGTH = 50
    private val SPACE_STR = "                                                                                  "

    private var app: String? = null

    private val logPreface: String
        get() {
            var str: String
            val stackIndex = 9
            val stackElement = Thread.currentThread().stackTrace
            var className = stackElement[stackIndex].className
            val index = className.lastIndexOf('.')
            val now = System.currentTimeMillis()
            if (index != -1)
                className = className.substring(index + 1)
            str = String.format(Locale.US, "%s: %s.%s:%d", app, className, stackElement[stackIndex].methodName, stackElement[stackIndex].lineNumber)
            if (str.length < PREFACE_LENGTH)
                str += SPACE_STR.substring(0, PREFACE_LENGTH - str.length)

            return str
        }

    fun init(context: Context) {
        app = context.getString(R.string.app_name)
        Timber.plant(CustomTree())
    }

    private class CustomTree : Timber.DebugTree() {
        override fun log(priority: Int, tag: String, message: String, t: Throwable?) {
            if (!BuildConfig.DEBUG && priority != android.util.Log.ERROR) {
                return
            }

            super.log(priority, logPreface, message, t)
        }
    }
}
