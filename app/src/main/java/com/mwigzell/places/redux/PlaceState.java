package com.mwigzell.places.redux;

import com.mwigzell.places.model.Place;
import com.mwigzell.places.redux.jedux.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mwigzell on 12/11/16.
 */

public class PlaceState implements State {
    public final List<Place> places;

    public PlaceState() { this(new ArrayList<Place>());}
    public PlaceState(PlaceState oldState, List<Place> places) {
        this(places); }
    public PlaceState(List<Place>places) {
        this.places = places;
    }
}
