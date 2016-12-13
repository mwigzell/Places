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
            case AppAction.INIT:
                nextState = new AppState();
                break;
            case AppAction.GET_PLACES:
                nextState = new AppState(state, AppState.States.GET_PLACES);
                break;
            case AppAction.GET_PLACES_FAILED:
                nextState = new AppState(state, AppState.States.GET_PLACES_FAILED);
                break;
            case AppAction.PLACES_DOWNLOADED:
                nextState = new AppState(state, AppState.States.PLACES_DOWNLOADED,
                        new PlaceState(state.placeState, action.places));
                break;
            default:
                nextState = super.call(action, state);
        }
        return nextState;
    }
}
