package com.mwigzell.places.dagger;

import android.content.Context;
import android.location.Location;

import com.mwigzell.places.model.Type;
import com.mwigzell.places.redux.AppAction;
import com.mwigzell.places.redux.AppReducer;
import com.mwigzell.places.redux.AppState;
import com.mwigzell.places.redux.ImmutableAppState;
import com.mwigzell.places.redux.PersistanceController;
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
    private Store<AppAction, AppState> store;
    private PersistanceController persistanceController;
    private AppState appState;

    public AppModule(Context context) {
        this.context = context;
        persistanceController = new PersistanceController(context);
        appState = persistanceController.getSavedState();
        if (appState == null) {
            appState = provideAppState();
        }
    }

    // need for testing, normally not for running except first time
    @Provides AppState provideAppState() {
        return ImmutableAppState.builder()
                .placeState(new PlaceState())
                .state(AppState.States.INIT)
                .lastError(new UnsupportedOperationException())
                .types(new ArrayList<Type>())
                .location(new Location("init"))
                .selectedType(new Type("init_name init_url"))
                .build();
    }

    @Provides @Singleton Context provideApplicationContext() { return context; }

    @Provides @Singleton
    public Store<AppAction, AppState> provideStore() {
        List<Reducer<AppAction, AppState>> reducers = new ArrayList<>();

        store =  new Store(new AppReducer(reducers), appState, new Logger("Places"), persistanceController);
        return store;
    }
}
