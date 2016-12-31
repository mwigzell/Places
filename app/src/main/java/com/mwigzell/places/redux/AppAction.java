package com.mwigzell.places.redux;

import android.location.Location;

import com.mwigzell.places.model.Place;
import com.mwigzell.places.model.Type;
import com.mwigzell.places.redux.original.Action;

import java.util.List;

import static com.mwigzell.places.redux.AppAction.Actions.GET_PLACES;
import static com.mwigzell.places.redux.AppAction.Actions.GET_PLACES_FAILED;
import static com.mwigzell.places.redux.AppAction.Actions.LOAD_TYPES;
import static com.mwigzell.places.redux.AppAction.Actions.PLACES_DOWNLOADED;
import static com.mwigzell.places.redux.AppState.States.TYPES_LOADED;

/**
 * Created by mwigzell on 12/10/16.
 */

public class AppAction implements Action {
    public enum Actions {
        INIT,
        GET_PLACES,
        GET_PLACES_FAILED,
        PLACES_DOWNLOADED,
        LOAD_TYPES,
        TYPES_LOADED,
        LOCATION_UPDATED,
        SELECT_TYPE
    }

    private final Actions type;

    public final List<Place> places;
    public final Throwable lastError;
    public final List<Type> types;
    public final Location location;
    public final Type selectedType;

    public AppAction(Actions type) {
        this(type, null, null, null, null, null);
    }

    public AppAction(Actions type, Throwable lastError) {
        this(type, null, lastError, null, null, null);
    }

    public AppAction(Actions type, List<Type> types) {
        this(type, null, null, types, null, null);
    }

    public AppAction(Actions type, Location location) {
        this(type, null, null, null, location, null);
    }

    public AppAction(Actions type, Type selectedType) {
        this(type, null, null, null, null, selectedType);
    }

    public AppAction(Actions type, List<Place> places, Throwable lastError, List<Type> types,
                     Location location, Type selectedType) {
        this.type = type;
        this.places = places;
        this.lastError = lastError;
        this.types = types;
        this.location = location;
        this.selectedType = selectedType;
    }

    public static AppAction placesDownloaded(List<Place> places) {
       return new AppAction(PLACES_DOWNLOADED, places, null, null, null, null);
    }

    public Actions getType() {
        return this.type;
    }
}
