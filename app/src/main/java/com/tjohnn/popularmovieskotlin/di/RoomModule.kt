package com.tjohnn.popularmovieskotlin.di

import android.arch.persistence.room.Room
import android.content.Context
import com.tjohnn.popularmovieskotlin.data.local.AppDatabase
import com.tjohnn.popularmovieskotlin.data.local.FavoriteMoviesDao

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Module
class RoomModule {

    @Singleton
    @Provides
    internal fun appDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java!!, AppDatabase.DATABASE_NAME).build()
    }

    @Singleton
    @Provides
    fun favoriteMoviesDao(appDatabase: AppDatabase): FavoriteMoviesDao {
        return appDatabase.favoriteMoviesDao
    }

}
