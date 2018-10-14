package com.tjohnn.popularmovieskotlin.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [(FavoriteMovie::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val favoriteMoviesDao: FavoriteMoviesDao

    companion object {

        const val DATABASE_NAME = "kotlin_popular_movies"
    }
}
