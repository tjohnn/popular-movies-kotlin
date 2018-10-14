package com.tjohnn.popularmovieskotlin.data.repository

import android.arch.lifecycle.LiveData


import com.tjohnn.popularmovieskotlin.data.dto.Movie
import com.tjohnn.popularmovieskotlin.data.local.FavoriteMovie
import com.tjohnn.popularmovieskotlin.data.local.FavoriteMoviesDao

import javax.inject.Inject
import javax.inject.Singleton

import io.reactivex.Completable
import io.reactivex.Flowable
import timber.log.Timber

@Singleton
class LocalMovieRepository @Inject
constructor(private val mFavoriteMoviesDao: FavoriteMoviesDao) {

    fun getFavoriteMovies(): Flowable<List<Movie>> = mFavoriteMoviesDao.favoriteMovies

    fun getFavoriteMovieById(id: Long): LiveData<FavoriteMovie> = mFavoriteMoviesDao.getFavoriteMovieById(id)

    fun createFavoriteMovie(favoriteMovie: FavoriteMovie): Completable {
        Timber.d("Fav movie created")
        return Completable.fromAction { mFavoriteMoviesDao.insertFavoriteMovie(favoriteMovie) }
    }

    fun updateFavoriteMovie(favoriteMovie: FavoriteMovie): Completable  =
         Completable.fromAction { mFavoriteMoviesDao.updateFavoriteMovie(favoriteMovie) }


    fun deleteFavoriteMovie(favoriteMovie: FavoriteMovie): Completable {
        Timber.d("Fav movie deleted")
        return Completable.fromAction { mFavoriteMoviesDao.deleteFavoriteMovie(favoriteMovie) }
    }
}
