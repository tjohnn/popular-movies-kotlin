package com.tjohnn.popularmovieskotlin.di

import android.app.Application
import android.content.Context

import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

@Module
class PicassoModule {

    @Singleton
    @Provides
    fun providePicasso(context: Application, okHttp3Downloader: OkHttp3Downloader): Picasso =
            Picasso.Builder(context).downloader(okHttp3Downloader).build()


    @Provides
    @Singleton
    fun okHttp3Downloader(okHttpClient: OkHttpClient): OkHttp3Downloader = OkHttp3Downloader(okHttpClient)


}
