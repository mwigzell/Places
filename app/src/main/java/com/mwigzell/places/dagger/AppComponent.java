package com.mwigzell.places.dagger;

import android.content.Context;

import com.mwigzell.places.Application;
import com.mwigzell.places.activities.MainActivity;
import com.mwigzell.places.activities.PlacesFragment;
import com.mwigzell.places.activities.PlacesViewAdapter;
import com.mwigzell.places.activities.TypesFragment;
import com.mwigzell.places.activities.TypesViewAdapter;
import com.mwigzell.places.data.DataService;
import com.mwigzell.places.data.LocationService;
import com.mwigzell.places.network.NetworkService;
import com.mwigzell.places.network.ServiceCreator;
import com.mwigzell.places.util.AndroidServices;
import com.mwigzell.places.util.FileUtils;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Provides common base of inject() signatures that serve both the runtime and mock
 * frameworks.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(Application app);
    void inject(MainActivity activity);
    void inject(ServiceCreator creator);
    void inject(FileUtils utils);
    void inject(AndroidServices services);
    void inject(NetworkService service);
    void inject(PlacesFragment places);
    void inject(TypesFragment types);
    void inject(DataService service);
    void inject(LocationService service);
    void inject(TypesViewAdapter adapter);

    Context context();
}
