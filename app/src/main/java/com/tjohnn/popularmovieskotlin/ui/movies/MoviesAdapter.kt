package com.tjohnn.popularmovieskotlin.ui.movies

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView

import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.tjohnn.popularmovieskotlin.R
import com.tjohnn.popularmovieskotlin.data.dto.Movie
import com.tjohnn.popularmovieskotlin.util.Constants

import butterknife.BindView
import butterknife.ButterKnife
import timber.log.Timber

class MoviesAdapter(
        private var mItems: List<Movie>,
        private val mListener: MovieItemLister,
        private val picasso: Picasso
): RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_movie_list, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindItem(mItems[position])
    }

    override fun getItemCount(): Int {
        return mItems.size
    }


    fun updateItems(movies: List<Movie>) {
        Timber.d(movies.toString())
        mItems = movies
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var movie: Movie
        @BindView(R.id.iv_poster)
        lateinit var posterView: ImageView
        @BindView(R.id.progress_bar_wrapper)
        lateinit var progressBar: FrameLayout

        var imageLoadCallBack: Callback = object : Callback {
            override fun onSuccess() {
                progressBar.visibility = View.GONE
            }

            override fun onError() {

            }
        }

        init {
            ButterKnife.bind(this, itemView)
            itemView.setOnClickListener { mListener.onMovieItemClicked(movie.id) }
            posterView.tag = imageLoadCallBack
        }

        fun bindItem(movie: Movie) {
            this.movie = movie
            picasso.load(Constants.POSTERS_BASE_URL + Constants.POSTERS_W_185 + movie.posterPath)
                    .error(R.drawable.placeholder_movie_image)
                    .into(posterView, imageLoadCallBack)
        }

    }


    interface MovieItemLister {
        fun onMovieItemClicked(movieId: Long)
    }
}
