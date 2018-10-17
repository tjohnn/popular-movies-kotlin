package com.tjohnn.popularmovieskotlin.ui.moviedetail

import dagger.Module

@Module
class MovieDetailModule {

    fun provideMovieId(movieDetailFragment: MovieDetailFragment): Long {
        return movieDetailFragment.arguments!!.getLong("movieId")
    }

}