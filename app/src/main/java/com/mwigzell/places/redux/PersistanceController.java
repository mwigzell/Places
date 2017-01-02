package com.mwigzell.places.redux;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mwigzell.places.redux.jedux.Action;
import com.mwigzell.places.redux.jedux.Store;
import com.mwigzell.places.redux.original.State;

public class PersistanceController implements Store.Middleware<Action, State> {

    private final SharedPreferences mPreferences;
    private final Gson mGson;

    public PersistanceController(Context context) {
        mPreferences = context.getSharedPreferences("data", 0);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapterFactory(new GsonAdaptersAppState());
        mGson = gsonBuilder.create();
    }

    public AppState getSavedState() {
        if (mPreferences.contains("data")) {
            String json = mPreferences.getString("data", "");
            return mGson.fromJson(json, ImmutableAppState.class);
            //return mGson.fromJson(json, AppState.class);
        }
        return null;
    }

    @Override
    public void dispatch(Store<Action, State> store, Action action, Store.NextDispatcher<Action> next) {
        next.dispatch(action);
        String json = mGson.toJson(store.getState());
        mPreferences.edit().putString("data", json).apply();
    }
}
