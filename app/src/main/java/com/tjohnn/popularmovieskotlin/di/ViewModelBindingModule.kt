package com.tjohnn.popularmovieskotlin.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tjohnn.popularmovieskotlin.ui.movies.MoviesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelBindingModule {

    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    abstract fun moviesViewModel(moviesViewModel: MoviesViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}