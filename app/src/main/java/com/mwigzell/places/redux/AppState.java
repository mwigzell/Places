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
public class AppState implements State {
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

    public final PlaceState placeState;
    public final States state;
    public final Throwable lastError;
    public final List<Type> types;
    public final Location location;
    public final Type selectedType;

    /*private void logStateChange(final AppState oldState, final States nextState) {
        Timber.d("State Change:");
        if (oldState != null)
            Timber.d("  Oldstate: %s", oldState.state.name());

        if (nextState != null)
            Timber.d("  New state: %s",nextState.name());
    }*/

    public AppState() {
        this(States.INIT);
    }
    public AppState(final States nextState) {
        this(nextState, null, null, null, null, null);
    }
    public AppState(final AppState oldState, final States nextState) {
        this(nextState, oldState.placeState, oldState.lastError, oldState.types, oldState.location, oldState.selectedType);
    }
    public AppState(final AppState oldState, final States nextState, final Throwable lastError) {
        this(nextState, oldState.placeState, lastError, oldState.types, oldState.location, oldState.selectedType);
    }
    public AppState(final AppState oldState, final States nextState, final PlaceState placeState) {
        this(nextState, placeState, oldState.lastError, oldState.types, oldState.location, oldState.selectedType);
    }
    public AppState(final AppState oldState, final States nextState, final List<Type> types) {
        this(nextState, oldState.placeState, oldState.lastError, types, oldState.location, oldState.selectedType);
    }
    public AppState(final AppState oldState, final States nextState, final Location location) {
        this(nextState, oldState.placeState, oldState.lastError, oldState.types, location, oldState.selectedType);
    }
    public AppState(final AppState oldState, final States nextState, final Type type) {
        this(nextState, oldState.placeState, oldState.lastError, oldState.types, oldState.location, type);
    }
    public AppState(final States nextState, final PlaceState placeState,
                    final Throwable lastError, final List<Type> types, final Location location, final Type selectedType) {
        state = nextState;
        this.placeState = placeState;
        this.lastError = lastError;
        this.types = types;
        this.location = location;
        this.selectedType = selectedType;

        //logStateChange(oldState, nextState);
    }

    public String toString() {
        return state.toString();
    }
}
