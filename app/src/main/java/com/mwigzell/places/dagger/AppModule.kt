package com.mwigzell.places.dagger

import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by mwigzell on 11/5/15.
 */
@Module
class AppModule() {
    @Provides
    @Named("RetrofitBaseUrl")
    fun getRetrofitBaseUrl(): String = "https://maps.googleapis.com"
}
