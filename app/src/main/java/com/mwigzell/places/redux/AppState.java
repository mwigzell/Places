package com.mwigzell.places.redux;

import com.mwigzell.places.redux.original.State;

import timber.log.Timber;

/**
 * Created by mwigzell on 12/10/16.
 */

public class AppState implements State {
    public enum States {
        INIT,
        GET_PLACES,
        GET_PLACES_FAILED,
        PLACES_DOWNLOADED
    }

    public final PlaceState placeState;
    public final States state;

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
        this(oldState, nextState, null);
    }
    public AppState(final AppState oldState, final States nextState, final PlaceState placeState) {
        state = nextState;
        this.placeState = placeState;

        logStateChange(oldState, nextState);
    }
}
