package com.tjohnn.popularmovieskotlin.ui

import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import com.tjohnn.popularmovieskotlin.R
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

        if (savedInstanceState == null) {
            findNavController(this, R.id.activity_main_nav_host_fragment).navigate(R.id.moviesFragment)
        }

        findNavController(this, R.id.activity_main_nav_host_fragment).addOnNavigatedListener { controller, destination ->
            when(destination.id) {
                R.id.movieDetailFragment -> showNavUp(true)
                R.id.moviesFragment -> showNavUp(false)
            }
        }
    }

    private fun showNavUp(show: Boolean) {
        supportActionBar?.run{
            setHomeButtonEnabled(show)
            setDisplayHomeAsUpEnabled(show)
            setDisplayShowHomeEnabled(show)
        }
    }

    override fun onSupportNavigateUp()
            = findNavController(this, R.id.activity_main_nav_host_fragment).navigateUp()


}
