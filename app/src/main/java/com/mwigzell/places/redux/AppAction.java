package com.mwigzell.places.redux;

import com.mwigzell.places.model.Place;
import com.mwigzell.places.redux.original.Action;

import java.util.List;

/**
 * Created by mwigzell on 12/10/16.
 */

public class AppAction implements Action {
    public static final String INIT = "INIT";
    public static final String GET_PLACES = "GET_PLACES";
    public static final String GET_PLACES_FAILED = "GET_PLACES_FAILED";
    public static final String PLACES_DOWNLOADED = "PLACES_DOWNLOADED";

    private final String type;

    public final List<Place> places;

    public AppAction(String type) {
        this(type, null);
    }

    public AppAction(String type, List<Place> places) {
        this.type = type;
        this.places = places;
    }

    public String getType() {
        return this.type;
    }

    public static AppAction initialize() {
        return new AppAction(INIT);
    }

    public static AppAction getPlaces() {
        return new AppAction(GET_PLACES);
    }

    public static AppAction getPlacesFailed() {
        return new AppAction(GET_PLACES_FAILED);
    }

    public static AppAction placesDownloaded(List<Place> places) {
        return new AppAction(PLACES_DOWNLOADED, places);
    }
}
