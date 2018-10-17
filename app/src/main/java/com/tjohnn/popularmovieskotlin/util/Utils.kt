package com.tjohnn.popularmovieskotlin.util

import android.net.Uri
import android.support.annotation.NonNull

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException

import java.io.IOException
import java.net.SocketTimeoutException

object Utils {

    @JvmStatic
    fun processNetworkError(throwable: Throwable): String? {
        return when (throwable) {
            is HttpException -> "Server error!! Please try later."
            is SocketTimeoutException -> "Network timeout! Ensure a better connection and retry."
            is IOException -> "Network error. Ensure you are connected to internet"
            else -> throwable.message
        }
    }

    @NonNull
    @JvmStatic
    fun getYoutubeVideoUri(videoId: String?) = Uri.parse("http://www.youtube.com/watch?v=$videoId")

}
