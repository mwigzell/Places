package com.mwigzell.places;

import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.mwigzell.places.model.Place;
import com.mwigzell.places.model.Type;
import com.mwigzell.places.redux.AppAction;
import com.mwigzell.places.redux.AppReducer;
import com.mwigzell.places.redux.AppState;
import com.mwigzell.places.redux.ImmutableAppState;
import com.mwigzell.places.redux.PersistanceController;
import com.mwigzell.places.redux.PlaceState;
import com.mwigzell.places.redux.jedux.Action;
import com.mwigzell.places.redux.jedux.Logger;
import com.mwigzell.places.redux.jedux.Store;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 *
 */
@RunWith(AndroidJUnit4.class)
public class PersistanceControllerTest {
    PersistanceController persistanceController;

    //@Mock
    //Store.NextDispatcher<Action> nextDispatcher;

    Store<AppAction, AppState> store;

    @Before
    public void setup() {
        //MockitoAnnotations.initMocks(this);
        persistanceController = new PersistanceController(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void dispatchTest() {
        List<Store.Reducer<AppAction, AppState>> reducers = new ArrayList<>();
        AppReducer appReducer = new AppReducer(reducers);
        Logger<AppAction, AppState> logger = new Logger("Test");
        AppState appState = ImmutableAppState.builder()
                .placeState(new PlaceState())
                .state(AppState.States.INIT)
                .lastError(new UnsupportedOperationException())
                .types(new ArrayList<Type>())
                .location(new Location("dummy"))
                .selectedType(new Type("name url"))
                .build();
        store = new Store(appReducer, appState, logger, persistanceController);
        List<Place> places = new ArrayList<>();
        Place place = new Place();
        place.name = "a_name";
        places.add(place);
        AppAction action = new AppAction(AppAction.Actions.PLACES_DOWNLOADED, places);
        store.dispatch(action);

        assertEquals(AppState.States.PLACES_DOWNLOADED, store.getState().state());

        AppState state = persistanceController.getSavedState();

        assertEquals(AppState.States.PLACES_DOWNLOADED, state.state());
        assertTrue(state.placeState().places.size() == 1);
        assertEquals("a_name", state.placeState().places.get(0).name);
    }
}
