package com.mwigzell.places;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.mwigzell.places.dagger.AppModule;
import com.mwigzell.places.util.HttpUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class HttpUtilsTest {
    private static AndroidTestComponent component = null;

    @Inject
    HttpUtils utils;

    @Before
    public void setup() {
        component = DaggerAndroidTestComponent.builder()
                .appModule(new AppModule(InstrumentationRegistry.getTargetContext()))
                .build();
        component.inject(this);
    }

    @Test
    public void testDumpUrlPlaces() throws Exception {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&types=food&name=harbour&key=AIzaSyDYvfBnjsQttjubdoLoyj5IBvf5_x86DtI";
        utils.dumpUrl(url);
        Thread.sleep(5000);
    }

    @Test
    public void testDumpUrlPhoto() throws Exception {
        String photoreference = "CoQBdwAAAJdyDOJlHLNbIjDXu_KjUMXYYs9SUP-qc-380i2wvbhQs3i4DTzHU_RFordaa-4FzaTlc-gh0mD9WpyNUB6b2hN6BsCpgq1AZf_C9uZZ08IrorEgJpjmzvDWYZUQ61Y3-V5_LU3wCIb_CzKvfaZxtt7rwX6xmrliGtAkftiulPNDEhDg0FldXTL5cWqq_h36DTYXGhR6EXckzVwRqU3_vfoWem4cmZ5_Jg";
        String restaurantpic = "https://maps.googleapis.com/maps/api/place/photo?" +
                "maxwidth=400" +
                "&photoreference=" +photoreference +
                "&key=" + Application.GOOGLE_PLACES_API_KEY;

        Timber.d("Loading restaurantpic=" + restaurantpic);
        utils.dumpUrl(restaurantpic);
        Thread.sleep(5000);
    }
}