package com.mwigzell.places.redux;

import com.mwigzell.places.model.Place;
import com.mwigzell.places.redux.jedux.Action;

import java.util.List;

import static com.mwigzell.places.redux.AppAction.Actions.PLACES_DOWNLOADED;

/**
 * Created by mwigzell on 12/10/16.
 */

public class AppAction<V> extends Action {
    public enum Actions {
        INIT,
        RESTART,
        GET_PLACES,
        GET_PLACES_FAILED,
        PLACES_DOWNLOADED,
        LOAD_TYPES,
        TYPES_LOADED,
        LOCATION_UPDATED,
        SELECT_TYPE
    }

    public AppAction(Actions type) {
        super(type);
    }

    public AppAction(Actions type, V value) {
        super(type, value);
    }

    public static AppAction placesDownloaded(List<Place> places) {
       return new AppAction(PLACES_DOWNLOADED, places);
    }

    public Actions getType() {
        return (Actions)type;
    }
}
