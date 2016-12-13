package com.mwigzell.places.dagger;

import android.content.Context;

import com.mwigzell.places.Application;
import com.mwigzell.places.activities.MainActivity;
import com.mwigzell.places.network.NetworkService;
import com.mwigzell.places.network.ServiceCreator;
import com.mwigzell.places.util.AndroidServices;
import com.mwigzell.places.util.FileUtils;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Provides common base of inject() signatures that serve both the runtime and mock
 * frameworks.
 * Created by mwigzell on 12/10/16.
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

    Context context();
}
