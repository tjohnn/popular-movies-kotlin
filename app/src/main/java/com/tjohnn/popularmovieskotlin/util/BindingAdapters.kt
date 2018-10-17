package com.tjohnn.popularmovieskotlin.util

import android.databinding.BindingAdapter
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.tjohnn.popularmovieskotlin.R
import com.tjohnn.popularmovieskotlin.data.dto.Movie
import com.tjohnn.popularmovieskotlin.data.dto.Review
import com.tjohnn.popularmovieskotlin.data.dto.Trailer
import com.tjohnn.popularmovieskotlin.ui.moviedetail.ReviewsAdapter
import com.tjohnn.popularmovieskotlin.ui.moviedetail.TrailersAdapter
import com.tjohnn.popularmovieskotlin.ui.movies.MoviesAdapter

object BindingAdapters {

    @BindingAdapter("app:items")
    @JvmStatic fun setItems(listView: AutoFitRecyclerView, items: List<Movie>?) {
        if(items == null) return
        with(listView.adapter as MoviesAdapter) {
            updateItems(items)
        }
    }

    @BindingAdapter("app:trailerItems")
    @JvmStatic fun setTrailerItems(listView: RecyclerView, items: List<Trailer>?) {
        if(items == null) return
        with(listView.adapter as TrailersAdapter) {
            updateItems(items)
        }
    }

    @BindingAdapter("app:reviewItems")
    @JvmStatic fun setReviewItems(listView: RecyclerView, items: List<Review>?) {
        if(items == null) return
        with(listView.adapter as ReviewsAdapter) {
            updateItems(items)
        }
    }

    @BindingAdapter("app:favoriteSrc")
    @JvmStatic fun setFavoriteSource(imageView: ImageView, isFavorite: Boolean) {
        if(isFavorite) imageView.setImageResource(R.drawable.ic_star_orange_24dp)
        else imageView.setImageResource(R.drawable.ic_star_border_orange_24dp)
    }
}