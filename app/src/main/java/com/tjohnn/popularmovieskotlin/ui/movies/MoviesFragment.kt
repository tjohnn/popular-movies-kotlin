package com.tjohnn.popularmovieskotlin.ui.movies

import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RadioGroup
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import butterknife.BindView
import butterknife.ButterKnife
import com.squareup.picasso.Picasso
import com.tjohnn.popularmovieskotlin.R
import com.tjohnn.popularmovieskotlin.data.PreferenceHelper
import com.tjohnn.popularmovieskotlin.data.dto.Movie
import com.tjohnn.popularmovieskotlin.databinding.FragmentMoviesBinding
import com.tjohnn.popularmovieskotlin.ui.MainActivity
import com.tjohnn.popularmovieskotlin.util.AutoFitRecyclerView
import com.tjohnn.popularmovieskotlin.util.Constants
import com.tjohnn.popularmovieskotlin.util.EventWrapper
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class MoviesFragment : DaggerFragment(), MoviesAdapter.MovieItemLister, SwipeRefreshLayout.OnRefreshListener {

    private val RECYCLER_VIEW_STATE_BUNDLE = "RecyclerViewLayoutBundle"
    private var isLoading = false

    @Inject
    lateinit var mainActivity: MainActivity

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    @Inject
    internal lateinit var picasso: Picasso

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var sortOrder: String
    private lateinit var moviesViewModel: MoviesViewModel

    private var moviesAdapter: MoviesAdapter? = null

    @BindView(R.id.rv_movies)
    lateinit var moviesRecyclerView: AutoFitRecyclerView

    @BindView(R.id.tv_message)
    lateinit var messageView: TextView

    @BindView(R.id.swipe_refresh)
    lateinit var swipeRefreshLayout: SwipeRefreshLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sortOrder = preferenceHelper.getMovieSortingOrder()
        setHasOptionsMenu(true)

        moviesViewModel = ViewModelProviders.of(this, viewModelFactory).get(MoviesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMoviesBinding = FragmentMoviesBinding.inflate(inflater)
        binding.viewModel = moviesViewModel
        binding.setLifecycleOwner(this)

        val view = binding.root
        ButterKnife.bind(this, view)

        moviesRecyclerView.setHasFixedSize(true)
        moviesAdapter = MoviesAdapter(emptyList(), this, picasso)
        moviesRecyclerView.adapter = moviesAdapter
        swipeRefreshLayout.setOnRefreshListener(this)


        moviesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!isLoading && !recyclerView.canScrollVertically(1) && Constants.BY_FAVOURITE != sortOrder) {
                    moviesViewModel.loadNextPage(sortOrder)
                }
            }
        })

        subscribeToMovieData()
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_movies_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if (id == R.id.sort_movies) {
            showSortingDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSortingDialog() {
        val dialog = Dialog(mainActivity)
        with (dialog) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_sort_movies)
            setTitle(R.string.select_sorting_type)
        }

        val radioGroup = dialog.findViewById<RadioGroup>(R.id.radio_group)
        radioGroup.check(when (sortOrder) {
            Constants.BY_POPULARITY_VALUE -> R.id.radio_popularity
            Constants.BY_TOP_RATED_VALUE -> R.id.radio_rating
            else -> R.id.radio_favorite
        })

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val order = when (checkedId) {
                R.id.radio_popularity -> Constants.BY_POPULARITY_VALUE
                R.id.radio_rating -> Constants.BY_TOP_RATED_VALUE
                else -> Constants.BY_FAVOURITE
            }
            preferenceHelper.updateSortingOrder(order)
            sortOrder = order
            subscribeToMovieData(true, false)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun subscribeToMovieData(forceRefresh: Boolean = false, isInitialCall: Boolean = true) {
        if (Constants.BY_FAVOURITE == sortOrder)  {
            moviesViewModel.loadFavoriteMovies()
        } else {
            moviesViewModel.loadMovies(sortOrder, forceRefresh, isInitialCall)
        }

        // set #isLoading to prevent loading more data while a request is going on
        moviesViewModel.isLoading.observe(this, Observer{
            isLoading = it ?: false
        })
        moviesViewModel.isLoading.observe(this, Observer{
            isLoading = it ?: false
        })

        moviesViewModel.openMovieDetail.observe(this, Observer<EventWrapper<Long>> {
            it?.getContentIfNotHandled()?.let {
                val bundle = Bundle()
                bundle.putLong("movieId", it)
                NavHostFragment.findNavController(this).navigate(R.id.action_moviesFragment_to_movieDetailFragment, bundle)
            }
        })
    }


    override fun onMovieItemClicked(movieId: Long) {
        moviesViewModel.movieItemClicked(movieId)
    }

    override fun onRefresh() {
        subscribeToMovieData(true)
        swipeRefreshLayout.isRefreshing = false
    }

    companion object {
        val TAG = MoviesFragment::class.java.simpleName
        fun newInstance() = MoviesFragment()
    }


}