package com.mwigzell.places;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mwigzell.places.dagger.AppModule;
import com.mwigzell.places.data.DataService;
import com.mwigzell.places.model.Type;
import com.mwigzell.places.redux.ActionCreator;
import com.mwigzell.places.redux.AppAction;
import com.mwigzell.places.redux.AppState;
import com.mwigzell.places.redux.jedux.Store;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 *
 */
@RunWith(AndroidJUnit4.class)
public class DataServiceTest {
    DataService dataService;
    SharedPreferences preferences;

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        preferences = context.getSharedPreferences("data", 0);
        preferences.edit().remove("data").commit();

        AppModule appModule = new AppModule(context);
        Store<AppAction<Object>, AppState> store = appModule.provideStore();
        ActionCreator actionCreator = new ActionCreator(store);
        dataService = new DataService(context, actionCreator, store);
    }

    @After
    public void teardown() {
        preferences.edit().remove("data").commit();
    }

    @Test
    public void loadTypes() throws Exception{
        List<Type> types = dataService.loadTypes();

        assertNotNull(types);
        assertTrue(types.size() > 0);
    }
}
