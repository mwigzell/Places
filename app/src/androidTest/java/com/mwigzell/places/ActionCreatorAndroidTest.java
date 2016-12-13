package com.mwigzell.places;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.mwigzell.places.dagger.AppModule;
import com.mwigzell.places.redux.ActionCreator;
import com.mwigzell.places.redux.AppAction;
import com.mwigzell.places.redux.AppState;
import com.mwigzell.places.redux.original.Store;
import com.mwigzell.places.redux.original.Subscriber;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ActionCreatorAndroidTest {
    Subscriber subscriber;

    private static AndroidTestComponent component = null;

    @Inject
    Store<AppAction, AppState> store;

    ActionCreator actionCreator;

    @Before
    public void setup() {
        component = DaggerAndroidTestComponent.builder()
                .appModule(new AppModule(InstrumentationRegistry.getTargetContext()))
                .build();
        component.inject(this);

        subscriber = mock(Subscriber.class);
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