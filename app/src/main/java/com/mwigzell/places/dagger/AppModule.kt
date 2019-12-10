package com.mwigzell.places.dagger

import android.content.Context
import androidx.room.Room
import com.mwigzell.places.repository.api.network.NetworkStatus
import com.mwigzell.places.repository.api.network.ServiceCreator
import com.mwigzell.places.repository.dao.*
import com.mwigzell.places.util.AndroidServices
import com.mwigzell.places.util.FileUtils
import dagger.Module
import dagger.Provides
import java.io.File
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
    fun getRetrofitBaseUrl(): String = RETROFIT_BASE_URL

    @Provides
    @Singleton
    fun providePlacesDao(placeDao: PlaceDaoGeneric): IPlaceDaoGeneric = placeDao

    @Provides
    @Named("RetrofitCacheDir")
    fun getRetrofitCacheDir(context: Context): File {
        return context.externalCacheDir!!
    }

    @Provides
    @Singleton
    fun getPlacesDatabase(context: Context): PlacesDatabase {
        return Room.databaseBuilder(context, PlacesDatabase::class.java, "PlacesDatabase").build()
    }

    @Provides
    @Singleton
    fun provideLocationDao(locationDao: LocationDaoGeneric): ILocationDaoGeneric = locationDao

    companion object {
        const val RETROFIT_BASE_URL = "https://maps.googleapis.com"
    }
}
