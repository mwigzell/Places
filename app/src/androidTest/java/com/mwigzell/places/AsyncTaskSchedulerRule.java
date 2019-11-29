package com.mwigzell.places;

import android.os.AsyncTask;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

/**
 * Test rule to override Rx schedulers with Scheduler from AsyncTask.THREAD_POOL_EXECUTOR.
 * The purpose is to set the AsyncTask scheduler into Rx Schedulers. This AsyncTask scheduler
 * has the special Android support for Espresso which obviates the need for implementing IdlingResources.
 */

public class AsyncTaskSchedulerRule implements TestRule {
    final Scheduler asyncTaskScheduler = Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR);

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
                    @Override
                    public Scheduler apply(Scheduler scheduler) throws Exception {
                        return asyncTaskScheduler;
                    }
                });
                RxJavaPlugins.setComputationSchedulerHandler(new Function<Scheduler, Scheduler>() {
                    @Override
                    public Scheduler apply(Scheduler scheduler) throws Exception {
                        return asyncTaskScheduler;
                    }
                });
                RxJavaPlugins.setNewThreadSchedulerHandler(new Function<Scheduler, Scheduler>() {
                    @Override
                    public Scheduler apply(Scheduler scheduler) throws Exception {
                        return asyncTaskScheduler;
                    }
                });
                RxJavaPlugins.setSingleSchedulerHandler(new Function<Scheduler, Scheduler>() {
                    @Override
                    public Scheduler apply(Scheduler scheduler) throws Exception {
                        return asyncTaskScheduler;
                    }
                });
                try {
                    base.evaluate();
                } finally {
                    RxAndroidPlugins.reset();
                    RxJavaPlugins.reset();
                }
            }
        };
    }
}