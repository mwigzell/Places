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
public interface TestComponent extends AppComponent {
    void inject(ActionCreatorTest test);
}