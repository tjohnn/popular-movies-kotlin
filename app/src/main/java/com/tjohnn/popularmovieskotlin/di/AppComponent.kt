package com.tjohnn.popularmovieskotlin.di

import android.app.Application
import com.tjohnn.popularmovieskotlin.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ActivityBindingModule::class, AndroidSupportInjectionModule::class,
    NetworkModule::class, PicassoModule::class, RoomModule::class])
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun build(): AppComponent
    }

    // override fun inject(app: App)
}