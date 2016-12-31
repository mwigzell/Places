package com.mwigzell.places;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.mwigzell.places.dagger.AppModule;
import com.mwigzell.places.data.DataService;
import com.mwigzell.places.redux.AppAction;
import com.mwigzell.places.redux.AppState;
import com.mwigzell.places.redux.original.Store;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import javax.inject.Inject;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 *
 */
@RunWith(AndroidJUnit4.class)
public class DataServiceTest {
    DataService dataService;

    @Before
    public void setup() {
        AndroidTestComponent component = DaggerAndroidTestComponent.builder()
                .appModule(new AppModule(InstrumentationRegistry.getTargetContext()))
                .build();
        component.inject(this);

        dataService = new DataService();
    }

    @Test
    public void loadTypes() throws Exception{
        List<String> types = dataService.loadTypes();

        assertNotNull(types);
        assertTrue(types.size() > 0);
    }
}
