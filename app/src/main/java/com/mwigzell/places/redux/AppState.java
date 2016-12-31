package com.mwigzell.places.redux;

import android.location.Location;

import com.mwigzell.places.model.Type;
import com.mwigzell.places.redux.original.State;

import java.util.List;

import timber.log.Timber;

/**
 * Created by mwigzell on 12/10/16.
 */

public class AppState implements State {
    public enum States {
        INIT,
        GET_PLACES,
        GET_PLACES_FAILED,
        PLACES_DOWNLOADED,
        LOAD_TYPES,
        TYPES_LOADED,
        LOCATION_UPDATED,
        SELECTED_TYPE
    }

    public final PlaceState placeState;
    public final States state;
    public final Throwable lastError;
    public final List<Type> types;
    public final Location location;
    public final Type selectedType;

    private void logStateChange(final AppState oldState, final States nextState) {
        Timber.d("State Change:");
        if (oldState != null)
            Timber.d("  Oldstate: %s", oldState.state.name());

        if (nextState != null)
            Timber.d("  New state: %s",nextState.name());
    }

    public AppState() {
        this(null, States.INIT);
    }
    public AppState(final AppState oldState, final States nextState) {
        this(oldState, nextState, null, null, null, null, null);
    }
    public AppState(final AppState oldState, final States nextState, final Throwable lastError) {
        this(oldState, nextState, null, lastError, null, null, null);
    }
    public AppState(final AppState oldState, final States nextState, final PlaceState placeState) {
        this(oldState, nextState, placeState, null, null, null, null);
    }
    public AppState(final AppState oldState, final States nextState, final List<Type> types) {
        this(oldState, nextState, null, null, types, null, null);
    }
    public AppState(final AppState oldState, final States nextState, final Location location) {
        this(oldState, nextState, null, null, null, location, null);
    }
    public AppState(final AppState oldState, final States nextState, final Type type) {
        this(oldState, nextState, null, null, null, null, type);
    }
    public AppState(final AppState oldState, final States nextState, final PlaceState placeState,
                    final Throwable lastError, final List<Type> types, final Location location, final Type selectedType) {
        state = nextState;
        this.placeState = placeState;
        this.lastError = lastError;
        this.types = types;
        this.location = location;
        this.selectedType = selectedType;

        logStateChange(oldState, nextState);
    }
}
