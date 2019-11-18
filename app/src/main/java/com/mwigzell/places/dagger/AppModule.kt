package com.mwigzell.places.dagger

import android.content.Context
import android.location.Location
import androidx.lifecycle.ViewModel
import com.mwigzell.places.redux.*
import com.mwigzell.places.redux.jedux.Logger
import com.mwigzell.places.redux.jedux.Store
import com.mwigzell.places.redux.jedux.Store.Reducer
import com.mwigzell.places.ui.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import java.util.*
import javax.inject.Named
import javax.inject.Singleton


/**
 * Created by mwigzell on 11/5/15.
 */
@Module
class AppModule() {

    @Provides
    @Singleton
    fun providePersistenceController(context: Context): PersistenceController {
        return PersistenceController(context)
    }

    // need for testing, normally not for running except first time
    @Provides
    fun provideAppState(persistenceController: PersistenceController): AppState {
        val appState = persistenceController.savedState

        return appState ?: ImmutableAppState.builder()
                .placeState(PlaceState())
                .state(AppState.States.INIT)
                .lastError("")
                .types(ArrayList())
                .location(Location("init"))
                .selectedPosition(0)
                .build()
    }

    @Provides
    @Singleton
    fun provideStore(appState: AppState, persistenceController: PersistenceController): Store<AppAction<Any>, AppState> {
        val reducers = ArrayList<Reducer<AppAction<*>, AppState>>()

        return Store<AppAction<Any>, AppState>(
                AppReducer(reducers),
                appState,
                Logger<AppAction<Any>, AppState>("Places"),
                persistenceController as Store.Middleware<AppAction<Any>, AppState>)
    }
}
