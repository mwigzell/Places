package com.mwigzell.places;

import com.crashlytics.android.Crashlytics;
import com.mwigzell.places.dagger.Injection;
import com.mwigzell.places.data.DataService;
import com.mwigzell.places.network.NetworkService;
import com.mwigzell.places.redux.ActionCreator;
import com.mwigzell.places.redux.AppAction;
import com.mwigzell.places.redux.AppState;
import com.mwigzell.places.redux.jedux.Store;
import com.mwigzell.places.redux.original.Subscriber;
import com.mwigzell.places.util.Log;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Created by mwigzell on 12/10/16.
 */

public class Application extends android.app.Application implements Subscriber {
    public static final String GOOGLE_PLACES_API_KEY = "AIzaSyDYvfBnjsQttjubdoLoyj5IBvf5_x86DtI";

    @Inject
    Store<AppAction, AppState> store;

    @Inject
    ActionCreator actionCreator;

    @Inject
    NetworkService networkService; // ensure construction

    @Inject
    DataService dataService; // ensure construction

    @Override
    public void onCreate() {
        super.onCreate();

        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
        Log.init(this);
        Timber.d("Hi ho!, its off to work we go!!!");

        Injection.create(this).getComponent().inject(this);

        store.subscribe(this);

        if (store.getState().state() == AppState.States.INIT) {
            actionCreator.init();
        } else {
            actionCreator.restart();
        }
    }

    @Override
    public void onStateChanged() {
        //Timber.d("Got state change");
    }
}
