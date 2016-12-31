package com.mwigzell.places;

import android.content.Context;
import android.location.Location;

import com.mwigzell.places.dagger.AppModule;
import com.mwigzell.places.dagger.Injection;
import com.mwigzell.places.model.Place;
import com.mwigzell.places.model.Type;
import com.mwigzell.places.redux.ActionCreator;
import com.mwigzell.places.redux.AppAction;
import com.mwigzell.places.redux.AppReducer;
import com.mwigzell.places.redux.AppState;
import com.mwigzell.places.redux.original.Reducer;
import com.mwigzell.places.redux.original.Store;
import com.mwigzell.places.redux.original.Subscriber;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ActionCreatorTest {
    @Mock
    Subscriber subscriber;

    private static TestComponent component = null;

    @Inject
    Store<AppAction, AppState> store;

    ActionCreator actionCreator;

    public Store<AppAction, AppState> provideStore() {
        List<Reducer<AppAction, AppState>> reducers = new ArrayList<>();
        store =  Store.create(new AppState(), new AppReducer(reducers));
        return store;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        component = DaggerTestComponent.builder()
                .appModule(new AppModule(Mockito.mock(Context.class)))
                .build();
        Injection.create(Mockito.mock(Context.class)).setComponent(component);
        component.inject(this);
        store = provideStore();
        actionCreator = new ActionCreator(store);
    }

    @Test
    public void testInit() throws Exception {
        store.subscribe(subscriber);
        actionCreator.init();

        assertEquals(AppState.States.INIT, store.getState().state);
        verify(subscriber).onStateChanged();
    }

    @Test
    public void testGetPlaces() throws Exception {
        store.subscribe(subscriber);
        actionCreator.getPlaces();

        assertEquals(AppState.States.GET_PLACES, store.getState().state);
        verify(subscriber).onStateChanged();
    }

    @Test
    public void testGetPlacesDownloaded() throws Exception {
        store.subscribe(subscriber);
        List<Place> places = new ArrayList<>();
        actionCreator.placesDownloaded(places);

        assertEquals(AppState.States.PLACES_DOWNLOADED, store.getState().state);
        assertEquals(places, store.getState().placeState.places);
        verify(subscriber).onStateChanged();
    }

    @Test
    public void testGetPlacesFailed() throws Exception {
        store.subscribe(subscriber);
        Throwable e = new Exception();
        actionCreator.getPlacesFailed(e);

        assertEquals(AppState.States.GET_PLACES_FAILED, store.getState().state);
        assertEquals(e, store.getState().lastError);
        verify(subscriber).onStateChanged();
    }

    @Test
    public void testLoadTypes() throws Exception {
        store.subscribe(subscriber);
        actionCreator.loadTypes();

        assertEquals(AppState.States.LOAD_TYPES, store.getState().state);
        verify(subscriber).onStateChanged();
    }

    @Test
    public void testTypesLoaded() throws Exception {
        store.subscribe(subscriber);
        List<Type> types = new ArrayList<>();
        actionCreator.typesLoaded(types);

        assertEquals(AppState.States.TYPES_LOADED, store.getState().state);
        assertEquals(types, store.getState().types);
        verify(subscriber).onStateChanged();
    }

    @Test
    public void testLocationUploaded() throws Exception {
        store.subscribe(subscriber);
        Location location = new Location("fused");
        actionCreator.locationUpdated(location);

        assertEquals(AppState.States.LOCATION_UPDATED, store.getState().state);
        assertEquals(location, store.getState().location);
        verify(subscriber).onStateChanged();
    }

    @Test public void selectType() {
        store.subscribe(subscriber);
        Type type = new Type("a_name an_url");
        actionCreator.selectType(type);

        assertEquals(AppState.States.SELECTED_TYPE, store.getState().state);
        assertEquals(type, store.getState().selectedType);
        verify(subscriber).onStateChanged();
    }
}