package com.tjohnn.popularmovieskotlin.util

import timber.log.Timber

/**
 * Created by Tjohn on 6/28/18.
 */


class TimberTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String? {
        return String.format("C:%s:%s",
                super.createStackElementTag(element),
                element.lineNumber)
    }

}
