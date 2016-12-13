package com.mwigzell.places.dagger;

import android.content.Context;


/**
 */

public class Injection {
    private AppComponent appComponent;
    private static Injection instance;

    private Injection(Context context) {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(context)).build();
    }

    public static Injection create(Context context) {
        if (instance == null) {
            instance = new Injection(context);
        }
        return instance;
    }

    public static Injection instance() { return instance; }

    public void destroy() {
        instance = null;
    }

    public AppComponent getComponent() { return appComponent; }

    public void setComponent(AppComponent component) { appComponent = component; }

}