package com.mwigzell.places.data;


import android.content.Context;
import android.os.AsyncTask;

import com.mwigzell.places.dagger.Injection;
import com.mwigzell.places.model.Type;
import com.mwigzell.places.redux.ActionCreator;
import com.mwigzell.places.redux.AppAction;
import com.mwigzell.places.redux.AppState;
import com.mwigzell.places.redux.jedux.Store;
import com.mwigzell.places.redux.original.Subscriber;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 *
 */
@Singleton
public class DataService implements Subscriber {
    Exception lastError;

    @Inject
    Store<AppAction, AppState> store;

    @Inject
    ActionCreator actionCreator;

    @Inject
    Context context;

    @Inject
    public DataService() {
        Injection.instance().getComponent().inject(this);
        store.subscribe(this);
    }

    public List<Type> loadTypes() throws Exception {
        List<Type> result = new ArrayList<>();
        BufferedReader input = new BufferedReader(new InputStreamReader(context.getAssets().open("types.txt")));
        String line;
        while((line = input.readLine()) != null) {
            result.add(new Type(line));
        }
        return result;
    }

    @Override
    public void onStateChanged() {
        //Timber.d("got state: " + store.getState().state);
        switch(store.getState().state) {
            case LOAD_TYPES:
                List<Type> types = store.getState().types;
                if (types == null || types.size() == 0) {
                    AsyncTask loadTypes = new AsyncTask<Object, Void, List<Type>>() {
                        @Override
                        protected List<Type> doInBackground(Object... params) {
                            List<Type> result = null;
                            try {
                                result = loadTypes();
                            } catch (Exception e) {
                                lastError = e;
                            }
                            return result;
                        }

                        @Override
                        protected void onPostExecute(List<Type> types) {
                            actionCreator.typesLoaded(types);
                        }
                    };
                    loadTypes.execute();
                } else {
                    actionCreator.typesLoaded(types);
                }
                break;
        }
    }
}
