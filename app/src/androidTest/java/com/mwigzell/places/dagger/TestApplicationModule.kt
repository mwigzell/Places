package com.mwigzell.places.dagger

import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class TestApplicationModule() {

    @Provides
    @Named("RetrofitBaseUrl")
    fun getRetrofitBaseUrl(): String = "http://localhost:8080/"
}