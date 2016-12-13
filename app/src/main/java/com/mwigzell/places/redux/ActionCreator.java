package com.mwigzell.places.redux;

import com.mwigzell.places.model.Place;
import com.mwigzell.places.redux.original.Store;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by mwigzell on 12/10/16.
 */

public class ActionCreator {
    private final Store<AppAction, AppState> store;

    @Inject
    public ActionCreator(Store<AppAction, AppState> store) {this.store = store;}

    public void init() {
        store.dispatch(AppAction.initialize());
    }

    public void getPlaces() {
        store.dispatch(AppAction.getPlaces());
    }

    public void getPlacesFailed() {
        store.dispatch(AppAction.getPlacesFailed());
    }

    public void placesDownloaded(List<Place> places) {
        store.dispatch(AppAction.placesDownloaded(places));
    }
}
