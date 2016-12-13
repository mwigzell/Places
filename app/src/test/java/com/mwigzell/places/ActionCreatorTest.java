package com.mwigzell.places;

import android.content.Context;

import com.mwigzell.places.dagger.AppModule;
import com.mwigzell.places.dagger.Injection;
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
}