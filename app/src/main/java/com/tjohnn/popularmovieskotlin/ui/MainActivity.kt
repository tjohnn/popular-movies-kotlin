package com.tjohnn.popularmovieskotlin.ui

import android.os.Bundle
import com.tjohnn.popularmovieskotlin.R
import com.tjohnn.popularmovieskotlin.ui.movies.MoviesFragment
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.content_main,
                    MoviesFragment.newInstance(),
                    MoviesFragment.TAG).commit()
        }
    }
}
