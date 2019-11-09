package com.mwigzell.places.network;

import com.mwigzell.places.PlacesApplication;
//import com.mwigzell.places.dagger.Injection;
import com.mwigzell.places.model.PlacesResponse;
import com.mwigzell.places.redux.ActionCreator;
import com.mwigzell.places.redux.AppAction;
import com.mwigzell.places.redux.AppState;
import com.mwigzell.places.redux.jedux.Store;
import com.mwigzell.places.redux.original.Subscriber;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@Singleton
public class NetworkService implements Subscriber {

    @Inject
    ServiceCreator serviceCreator;

    @Inject
    ActionCreator actionCreator;

    @Inject
    Store<AppAction<Object>, AppState> store;

    private ServiceCreator.PlacesClient client;

    @Inject
    public NetworkService() {
        store.subscribe(this);
        client = serviceCreator.createService(ServiceCreator.PlacesClient.class);
    }

    public void getPlaces() {
        getPlaces("-33.8670522,151.1957362", "500", "food&name=harbour");
    }

    public void getPlaces(String location, String radius, String type) {
        Timber.d("Get places radius=" + radius + " type=" + type);

        client.getPlaces(location, radius, type, PlacesApplication.Companion.getGOOGLE_PLACES_API_KEY())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new rx.Subscriber<PlacesResponse>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("  completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "  error with exception");
                        actionCreator.getPlacesFailed(e);
                    }

                    @Override
                    public void onNext(PlacesResponse response) {
                        Timber.d("Got places status:" + response.status);
                        actionCreator.placesDownloaded(response.results);
                    }
                });
    }

    @Override
    public void onStateChanged() {
        AppState.States state = store.getState().state();
        //Timber.d("State Changed: %s",state.name());
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
