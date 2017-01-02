package com.mwigzell.places.redux;

import android.location.Location;

import com.mwigzell.places.model.Place;
import com.mwigzell.places.model.Type;
import com.mwigzell.places.redux.original.CombinedReducers;
import com.mwigzell.places.redux.jedux.Store.Reducer;

import java.util.List;

public class AppReducer extends CombinedReducers<AppAction, AppState> implements Reducer<AppAction, AppState> {

    public AppReducer(List<? extends Reducer<AppAction, AppState>> reducers) {
        super(reducers);
    }

    @Override
    public AppState reduce(AppAction action, AppState state) {
        AppState nextState = null;
        switch(action.getType()) {
            case INIT:
                nextState = ImmutableAppState.copyOf(state).withState(AppState.States.INIT);
                break;
            case RESTART:
                nextState = ImmutableAppState.copyOf(state).withState(AppState.States.RESTARTED);
                break;
            case GET_PLACES:
                nextState = ImmutableAppState.copyOf(state).withState(AppState.States.GET_PLACES);
                break;
            case GET_PLACES_FAILED:
                nextState = ImmutableAppState.copyOf(state).withState(AppState.States.GET_PLACES_FAILED)
                    .withLastError((Throwable)action.value);
                break;
            case PLACES_DOWNLOADED:
                nextState = ImmutableAppState.copyOf(state).withState(AppState.States.PLACES_DOWNLOADED)
                        .withPlaceState(new PlaceState(state.placeState(), (List<Place>)action.value));
                break;
            case LOAD_TYPES:
                nextState = ImmutableAppState.copyOf(state).withState(AppState.States.LOAD_TYPES);
                break;
            case TYPES_LOADED:
                nextState = ImmutableAppState.copyOf(state).withState(AppState.States.TYPES_LOADED)
                    .withTypes((List<Type>)action.value);
                break;
            case LOCATION_UPDATED:
                nextState = ImmutableAppState.copyOf(state).withState(AppState.States.LOCATION_UPDATED)
                    .withLocation((Location)action.value);
                break;
            case SELECT_TYPE:
                nextState = ImmutableAppState.copyOf(state).withState(AppState.States.SELECTED_TYPE)
                    .withSelectedType((Type)action.value);
                break;
            default:
                nextState = super.reduce(action, state);
        }
        return nextState;
    }
}
