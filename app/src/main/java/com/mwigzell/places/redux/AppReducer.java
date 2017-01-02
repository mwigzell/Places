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
                nextState = new AppState();
                break;
            case RESTART:
                nextState = new AppState(AppState.States.RESTARTED);
                break;
            case GET_PLACES:
                nextState = new AppState(state, AppState.States.GET_PLACES);
                break;
            case GET_PLACES_FAILED:
                nextState = new AppState(state, AppState.States.GET_PLACES_FAILED, (Throwable)action.value);
                break;
            case PLACES_DOWNLOADED:
                nextState = new AppState(state, AppState.States.PLACES_DOWNLOADED,
                        new PlaceState(state.placeState, (List<Place>)action.value));
                break;
            case LOAD_TYPES:
                nextState = new AppState(state, AppState.States.LOAD_TYPES);
                break;
            case TYPES_LOADED:
                nextState = new AppState(state, AppState.States.TYPES_LOADED, (List<Type>)action.value);
                break;
            case LOCATION_UPDATED:
                nextState = new AppState(state, AppState.States.LOCATION_UPDATED, (Location)action.value);
                break;
            case SELECT_TYPE:
                nextState = new AppState(state, AppState.States.SELECTED_TYPE, (Type)action.value);
                break;
            default:
                nextState = super.reduce(action, state);
        }
        return nextState;
    }
}
