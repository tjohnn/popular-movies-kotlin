package com.tjohnn.popularmovieskotlin.ui.moviedetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.internal.Utils
import com.squareup.picasso.Picasso
import com.tjohnn.popularmovieskotlin.R
import com.tjohnn.popularmovieskotlin.data.dto.Movie
import com.tjohnn.popularmovieskotlin.data.dto.Trailer
import com.tjohnn.popularmovieskotlin.databinding.FragmentMoviesDetailBinding
import com.tjohnn.popularmovieskotlin.ui.moviedetail.TrailersAdapter.OnTrailerItemListener
import com.tjohnn.popularmovieskotlin.util.Constants.POSTERS_BASE_URL
import com.tjohnn.popularmovieskotlin.util.Constants.POSTERS_W_342
import com.tjohnn.popularmovieskotlin.util.Constants.POSTERS_W_500
import com.tjohnn.popularmovieskotlin.util.Utils.getYoutubeVideoUri
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_movies_detail.view.*
import javax.inject.Inject

class MovieDetailFragment: DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var picasso: Picasso

    var movieId: Long = 0

    @BindView(R.id.iv_poster)
    lateinit var posterImageView: ImageView

    @BindView(R.id.iv_backdrop)
    lateinit var backdropImageView: ImageView

    @BindView(R.id.rv_reviews)
    lateinit var reviewsRecyclerView: RecyclerView

    @BindView(R.id.rv_trailers)
    lateinit var trailerRecyclerView: RecyclerView

    lateinit var mViewModel: MovieDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = viewModelFactory.create(MovieDetailViewModel::class.java)
        movieId = arguments!!.getLong("movieId")


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMoviesDetailBinding = FragmentMoviesDetailBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModel = mViewModel
        val view: View = binding.root
        ButterKnife.bind(this, view)
        setupRecyclerViews()
        mViewModel.loadMovieDetail(movieId)

        subscribeToViewModel()
        return view
    }


    private fun setupRecyclerViews() {
        with(reviewsRecyclerView){
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            adapter = ReviewsAdapter(arrayListOf())
        }
        with(trailerRecyclerView){
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            adapter = TrailersAdapter(arrayListOf(), object : OnTrailerItemListener{
                override fun onTrailerItemClicked(trailer: Trailer) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = getYoutubeVideoUri(trailer.source)
                    startActivity(intent)
                }

            })
        }
    }

    private fun subscribeToViewModel() {
        // Observing movie data so I can use injected picasso instance to load pictures
        mViewModel.movieLiveData.observe(this, Observer<Movie> {
            picasso.load(TextUtils.concat(
                    POSTERS_BASE_URL,
                    POSTERS_W_500,
                    it?.backdropPath).toString())
                    .error(R.drawable.backdrop_placeholder).into(backdropImageView)

            picasso.load(TextUtils.concat(
                    POSTERS_BASE_URL,
                    POSTERS_W_342,
                    it?.posterPath).toString())
                    .error(R.drawable.placeholder_movie_image).into(posterImageView)
        })
    }


    companion object {
        const val TAG: String = "MovieDetailFragment"
    }

}