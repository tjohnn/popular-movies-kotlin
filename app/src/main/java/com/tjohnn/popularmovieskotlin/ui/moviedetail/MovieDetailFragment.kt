package com.tjohnn.popularmovieskotlin.ui.moviedetail

import com.tjohnn.popularmovieskotlin.data.dto.Movie
import dagger.android.support.DaggerFragment

class MovieDetailFragment: DaggerFragment() {


    companion object {
        const val TAG: String = "MovieDetailFragment"

        fun getInstance(movie: Movie){

        }
    }
}