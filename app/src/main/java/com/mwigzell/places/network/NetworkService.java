package com.mwigzell.places.network;

import com.mwigzell.places.Application;
import com.mwigzell.places.dagger.Injection;
import com.mwigzell.places.model.PlacesResponse;
import com.mwigzell.places.redux.ActionCreator;
import com.mwigzell.places.redux.AppAction;
import com.mwigzell.places.redux.AppState;
import com.mwigzell.places.redux.original.Store;
import com.mwigzell.places.redux.original.Subscriber;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class NetworkService implements Subscriber {

    @Inject
    ServiceCreator serviceCreator;

    @Inject
    ActionCreator actionCreator;

    @Inject
    Store<AppAction, AppState> store;

    private ServiceCreator.PlacesClient client;

    @Inject
    public NetworkService() {
        Injection.instance().getComponent().inject(this);
        store.subscribe(this);
        client = serviceCreator.createService(ServiceCreator.PlacesClient.class);
    }


    public void getPlaces() {
        Timber.d("Get places");

        client.getPlaces("-33.8670522,151.1957362", "500", "food&name=harbour", Application.GOOGLE_PLACES_API_KEY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                //.subscribe(new rx.Subscriber<List<Clinic>>() {
                .subscribe(new rx.Subscriber<PlacesResponse>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("  completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "  error with exception");
                        //actionCreator.fetchClinicsFailed(e.getMessage());
                    }

                    @Override
                    public void onNext(PlacesResponse places) {
                        Timber.d("Got places=" + places);
                        actionCreator.placesDownloaded(places.results);
                    }
                });
    }

    @Override
    public void onStateChanged() {
        AppState.States state = store.getState().state;
        Timber.e("State Changed: %s",state.name());
        if (state == AppState.States.GET_PLACES) {
            getPlaces();
        }
    }

    public void clearCache() {
        serviceCreator.clearCacheForOkHTTP();
    }

    public void close() {
        serviceCreator.clearCacheForOkHTTP();
        serviceCreator.close();
    }
}
