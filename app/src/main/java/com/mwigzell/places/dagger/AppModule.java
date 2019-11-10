package com.mwigzell.places.dagger;

import android.content.Context;
import android.location.Location;

import com.mwigzell.places.model.Type;
import com.mwigzell.places.redux.AppAction;
import com.mwigzell.places.redux.AppReducer;
import com.mwigzell.places.redux.AppState;
import com.mwigzell.places.redux.ImmutableAppState;
import com.mwigzell.places.redux.PersistenceController;
import com.mwigzell.places.redux.PlaceState;
import com.mwigzell.places.redux.jedux.Logger;
import com.mwigzell.places.redux.jedux.Store.Reducer;
import com.mwigzell.places.redux.jedux.Store;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mwigzell on 11/5/15.
 */
@Module
public class AppModule {
    private Context context;
    private Store<AppAction<Object>, AppState> store;
    private PersistenceController persistenceController;
    private AppState appState;

    public AppModule(Context context) {
        this.context = context;
        persistenceController = new PersistenceController(context);
        appState = persistenceController.getSavedState();
        if (appState == null) {
            appState = provideAppState();
        }
    }

    // need for testing, normally not for running except first time
    @Provides
    public AppState provideAppState() {
        return ImmutableAppState.builder()
                .placeState(new PlaceState())
                .state(AppState.States.INIT)
                .lastError("")
                .types(new ArrayList<Type>())
                .location(new Location("init"))
                .selectedPosition(new Integer(0))
                .build();
    }

    @Provides @Singleton
    public Store<AppAction<Object>, AppState> provideStore() {
        List<Reducer<AppAction, AppState>> reducers = new ArrayList<>();

        store =  new Store(new AppReducer(reducers), appState, new Logger("Places"), persistenceController);
        return store;
    }
}
