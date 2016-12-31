package com.mwigzell.places;

import com.mwigzell.places.dagger.AppComponent;
import com.mwigzell.places.dagger.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by mwigzell on 12/12/16.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AndroidTestComponent extends AppComponent {
    void inject(ActionCreatorAndroidTest test);
    void inject(HttpUtilsTest test);
    void inject(DataServiceTest test);
}