package com.tjohnn.popularmovieskotlin.data

import android.content.Context
import android.content.SharedPreferences


import com.tjohnn.popularmovieskotlin.util.Constants

import javax.inject.Inject
import javax.inject.Singleton

import android.content.Context.MODE_PRIVATE

@Singleton
class PreferenceHelper @Inject constructor(context: Context) {

    private val preferences: SharedPreferences


    init {
        preferences = context.getSharedPreferences(APP_PREF_NAME, MODE_PRIVATE)
    }

    fun getMovieSortingOrder(): String = preferences.getString(SORTING_ORDER_PREF_KEY, null) ?: Constants.BY_POPULARITY_VALUE


    fun updateSortingOrder(value: String) {
        preferences.edit().putString(SORTING_ORDER_PREF_KEY, value).apply()
    }

    companion object {

        private const val APP_PREF_NAME = "PopularMovies"
        private const val SORTING_ORDER_PREF_KEY = "SortingOrderKey"
    }

}
