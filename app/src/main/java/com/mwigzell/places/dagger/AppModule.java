package com.mwigzell.places.dagger;

import android.content.Context;

import com.mwigzell.places.redux.AppAction;
import com.mwigzell.places.redux.AppReducer;
import com.mwigzell.places.redux.AppState;
import com.mwigzell.places.redux.original.Reducer;
import com.mwigzell.places.redux.original.Store;

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

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides @Singleton Context provideApplicationContext() { return context; }

    @Provides @Singleton
    public Store<AppAction, AppState> provideStore() {
        List<Reducer<AppAction, AppState>> reducers = new ArrayList<>();

        store =  Store.create(new AppState(), new AppReducer(reducers));
        return store;
    }
}
