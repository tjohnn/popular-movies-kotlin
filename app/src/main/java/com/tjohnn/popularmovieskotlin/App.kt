package com.tjohnn.popularmovieskotlin

import com.tjohnn.popularmovieskotlin.di.DaggerAppComponent
import com.tjohnn.popularmovieskotlin.util.TimberTree
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class App : DaggerApplication() {

    lateinit var injector: AndroidInjector<out DaggerApplication>

    override fun applicationInjector(): AndroidInjector<out DaggerApplication>? {
        return injector
    }

    override fun onCreate() {
        injector = DaggerAppComponent.builder().application(this).build()
        super.onCreate()

        if (BuildConfig.DEBUG)
            Timber.plant(TimberTree())
    }
}