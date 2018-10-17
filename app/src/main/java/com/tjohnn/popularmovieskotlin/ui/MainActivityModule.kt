package com.tjohnn.popularmovieskotlin.ui

import com.tjohnn.popularmovieskotlin.di.FragmentScoped
import com.tjohnn.popularmovieskotlin.ui.moviedetail.MovieDetailFragment
import com.tjohnn.popularmovieskotlin.ui.moviedetail.MovieDetailModule
import com.tjohnn.popularmovieskotlin.ui.movies.MoviesFragment
import com.tjohnn.popularmovieskotlin.ui.movies.MoviesModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = [MoviesModule::class])
    abstract fun moviesFragment(): MoviesFragment

    @FragmentScoped
    @ContributesAndroidInjector(modules = [MovieDetailModule::class])
    abstract fun movieDetailFragment(): MovieDetailFragment

    companion object
}