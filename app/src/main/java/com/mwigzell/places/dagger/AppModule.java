package com.mwigzell.places.dagger;

import android.content.Context;

import com.mwigzell.places.redux.AppAction;
import com.mwigzell.places.redux.AppReducer;
import com.mwigzell.places.redux.AppState;
import com.mwigzell.places.redux.PersistanceController;
import com.mwigzell.places.redux.jedux.Logger;
import com.mwigzell.places.redux.jedux.Store.Reducer;
import com.mwigzell.places.redux.jedux.Store;
import com.mwigzell.places.redux.original.State;

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
        State state = persistanceController.getSavedState();
        if (state == null) {
            appState = new AppState();
        } else {
            appState = (AppState)state;
        }
    }

    @Provides @Singleton Context provideApplicationContext() { return context; }

    @Provides @Singleton
    public Store<AppAction, AppState> provideStore() {
        List<Reducer<AppAction, AppState>> reducers = new ArrayList<>();

        store =  new Store(new AppReducer(reducers), appState, new Logger("Places"));
        return store;
    }
}
