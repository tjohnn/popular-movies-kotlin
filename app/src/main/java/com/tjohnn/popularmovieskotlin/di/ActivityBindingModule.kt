package com.tjohnn.popularmovieskotlin.di

import com.tjohnn.popularmovieskotlin.ui.MainActivity
import com.tjohnn.popularmovieskotlin.ui.MainActivityModule

import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    internal abstract fun mainActivity(): MainActivity
}
