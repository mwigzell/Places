package com.mwigzell.places

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import androidx.test.runner.AndroidJUnitRunner
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers

class CustomTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, TestApplication::class.java.name, context)
    }

    override fun onStart() {
        RxJavaHooks.setOnIOScheduler { Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR) }
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline())
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        RxJavaHooks.reset()
    }
}