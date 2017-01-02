package com.mwigzell.places.redux;

import android.location.Location;

import com.mwigzell.places.model.Type;
import com.mwigzell.places.redux.original.State;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

import timber.log.Timber;

/**
 * Created by mwigzell on 12/10/16.
 */
@Value.Immutable
@Gson.TypeAdapters
public abstract class AppState implements State {
    public enum States {
        INIT,
        RESTARTED,
        GET_PLACES,
        GET_PLACES_FAILED,
        PLACES_DOWNLOADED,
        LOAD_TYPES,
        TYPES_LOADED,
        LOCATION_UPDATED,
        SELECTED_TYPE
    }

    public abstract PlaceState placeState();
    public abstract AppState.States state();
    public abstract Throwable lastError();
    public abstract List<Type> types();
    public abstract Location location();
    public abstract Type selectedType();


    public String toString() {
        return state().toString();
    }
}
