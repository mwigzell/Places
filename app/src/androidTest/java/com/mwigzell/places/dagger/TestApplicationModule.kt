package com.mwigzell.places.dagger

import android.content.Context
import com.mwigzell.places.repository.PlacesDao
import com.mwigzell.places.repository.api.network.NetworkStatus
import com.mwigzell.places.util.AndroidServices
import dagger.Module
import dagger.Provides
import org.mockito.Mockito
import javax.inject.Named
import javax.inject.Singleton

@Module
class TestApplicationModule() {

    @Provides
    @Singleton
    fun provideNetworkStatus(context: Context): NetworkStatus {
        return AndroidServices(context)
    }

    @Provides
    @Named("RetrofitBaseUrl")
    fun getRetrofitBaseUrl(): String = "http://localhost:8080/"

    @Provides
    @Singleton
    fun providePlacesDao(): PlacesDao {
        return Mockito.mock(PlacesDao::class.java)
    }
}