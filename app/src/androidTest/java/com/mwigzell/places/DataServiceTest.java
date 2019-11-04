package com.mwigzell.places;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mwigzell.places.dagger.AppModule;
import com.mwigzell.places.data.DataService;
import com.mwigzell.places.model.Type;

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

    @Before
    public void setup() {
        AndroidTestComponent component = DaggerAndroidTestComponent.builder()
                .appModule(new AppModule(InstrumentationRegistry.getInstrumentation().getTargetContext()))
                .build();
        component.inject(this);

        dataService = new DataService();
    }

    @Test
    public void loadTypes() throws Exception{
        List<Type> types = dataService.loadTypes();

        assertNotNull(types);
        assertTrue(types.size() > 0);
    }
}
