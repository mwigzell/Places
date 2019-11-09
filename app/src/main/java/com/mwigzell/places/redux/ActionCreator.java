package com.mwigzell.places.redux;

import android.location.Location;

import com.mwigzell.places.model.Type;
import com.mwigzell.places.model.Place;
import com.mwigzell.places.redux.jedux.Store;

import java.util.List;

import javax.inject.Inject;

import static com.mwigzell.places.redux.AppAction.Actions.GET_PLACES;
import static com.mwigzell.places.redux.AppAction.Actions.GET_PLACES_FAILED;
import static com.mwigzell.places.redux.AppAction.Actions.INIT;
import static com.mwigzell.places.redux.AppAction.Actions.LOAD_TYPES;
import static com.mwigzell.places.redux.AppAction.Actions.LOCATION_UPDATED;
import static com.mwigzell.places.redux.AppAction.Actions.RESTART;
import static com.mwigzell.places.redux.AppAction.Actions.SELECT_TYPE;
import static com.mwigzell.places.redux.AppAction.Actions.TYPES_LOADED;

/**
 * Created by mwigzell on 12/10/16.
 */

public class ActionCreator {
    private final Store<AppAction<Object>, AppState> store;

    @Inject
    public ActionCreator(Store<AppAction<Object>, AppState> store) {this.store = store;}

    public void init() {
        store.dispatch(new AppAction(INIT));
    }

    public void getPlaces() {
        store.dispatch(new AppAction(GET_PLACES));
    }

    public void getPlacesFailed(final Throwable lastError) {
        store.dispatch(new AppAction(GET_PLACES_FAILED, lastError));
    }

    public void placesDownloaded(List<Place> places) {
        store.dispatch(AppAction.placesDownloaded(places));
    }

    public void loadTypes() {
        store.dispatch(new AppAction(LOAD_TYPES));
    }

    public void typesLoaded(List<Type> types) {
        store.dispatch(new AppAction(TYPES_LOADED, types));
    }

    public void locationUpdated(Location location) {
        store.dispatch(new AppAction(LOCATION_UPDATED, location));
    }

    public void selectType(Type type) {
        store.dispatch(new AppAction(SELECT_TYPE, type));
    }

    public void restart() { store.dispatch(new AppAction(RESTART)); }
}
