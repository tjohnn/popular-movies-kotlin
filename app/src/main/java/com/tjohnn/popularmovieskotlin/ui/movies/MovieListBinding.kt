package com.tjohnn.popularmovieskotlin.ui.movies

import android.databinding.BindingAdapter
import com.tjohnn.popularmovieskotlin.data.dto.Movie
import com.tjohnn.popularmovieskotlin.util.AutoFitRecyclerView

object MovieListBinding {

    @BindingAdapter("app:items")
    @JvmStatic fun setItems(listView: AutoFitRecyclerView, items: List<Movie>?) {
        if(items == null) return
        with(listView.adapter as MoviesAdapter) {
            updateItems(items)
        }
    }
}