package com.mwigzell.places.dagger

import android.content.Context
import com.mwigzell.places.repository.PlacesDao
import com.mwigzell.places.repository.api.dao.PlacesDaoImpl
import com.mwigzell.places.repository.api.network.NetworkStatus
import com.mwigzell.places.util.AndroidServices
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
    @Singleton
    fun provideNetworkStatus(context: Context): NetworkStatus {
        return AndroidServices(context)
    }

    @Provides
    @Named("RetrofitBaseUrl")
    fun getRetrofitBaseUrl(): String = "https://maps.googleapis.com"

    @Provides
    @Singleton
    fun providePlacesDao(placesDao: PlacesDaoImpl): PlacesDao = placesDao
}
