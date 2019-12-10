package com.mwigzell.places.dagger

import android.content.Context
import com.mwigzell.places.repository.dao.IPlaceDaoGeneric
import com.mwigzell.places.repository.api.network.NetworkStatus
import com.mwigzell.places.repository.dao.ILocationDaoGeneric
import com.mwigzell.places.util.AndroidServices
import dagger.Module
import dagger.Provides
import org.mockito.Mockito
import java.io.File
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
    fun getRetrofitBaseUrl(): String = RETROFIT_BASE_URL

    @Provides
    @Singleton
    fun providePlacesDao(): IPlaceDaoGeneric {
        return Mockito.mock(IPlaceDaoGeneric::class.java)
    }

    @Provides
    @Named("RetrofitCacheDir")
    fun getRetrofitCacheDir(context: Context): File {
        return context.externalCacheDir
    }

    @Provides
    @Singleton
    fun provideLocationDao(): ILocationDaoGeneric {
        return Mockito.mock(ILocationDaoGeneric::class.java)
    }

    companion object {
        const val RETROFIT_BASE_URL = "http://localhost:8080/"
    }
}