package com.mwigzell.places.redux;

import com.mwigzell.places.redux.original.CombinedReducers;
import com.mwigzell.places.redux.original.Reducer;

import java.util.List;

public class AppReducer extends CombinedReducers<AppAction, AppState> implements Reducer<AppAction, AppState> {

    public AppReducer(List<? extends Reducer<AppAction, AppState>> reducers) {
        super(reducers);
    }

    @Override
    public AppState call(AppAction action, AppState state) {
        AppState nextState = null;
        switch(action.getType()) {
            case INIT:
                nextState = new AppState();
                break;
            case GET_PLACES:
                nextState = new AppState(state, AppState.States.GET_PLACES);
                break;
            case GET_PLACES_FAILED:
                nextState = new AppState(state, AppState.States.GET_PLACES_FAILED, action.lastError);
                break;
            case PLACES_DOWNLOADED:
                nextState = new AppState(state, AppState.States.PLACES_DOWNLOADED,
                        new PlaceState(state.placeState, action.places));
                break;
            case LOAD_TYPES:
                nextState = new AppState(state, AppState.States.LOAD_TYPES);
                break;
            case TYPES_LOADED:
                nextState = new AppState(state, AppState.States.TYPES_LOADED, action.types);
                break;
            case LOCATION_UPDATED:
                nextState = new AppState(state, AppState.States.LOCATION_UPDATED, action.location);
                break;
            case SELECT_TYPE:
                nextState = new AppState(state, AppState.States.SELECTED_TYPE, action.selectedType);
                break;
            default:
                nextState = super.call(action, state);
        }
        return nextState;
    }
}
