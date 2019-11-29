package com.mwigzell.places.dagger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mwigzell.places.ui.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import org.mockito.Mockito
import org.mockito.Mockito.mock
import javax.inject.Provider

@Module
class MockViewModelModule {
    @Provides
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun getMainViewModel(): ViewModel {
        return mainViewModel
    }

    //val viewModelFactory: ViewModelProvider.Factory = mock(ViewModelProvider.Factory::class.java)

    @Provides
    fun getViewModelFactory(map: Map<Class<out ViewModel>,
            @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory {
        return ViewModelFactory(map)
        //return viewModelFactory
    }

    companion object {
        val mainViewModel = mock(MainViewModel::class.java)
    }
}