package com.tjohnn.popularmovieskotlin.ui.movies

import android.app.Application
import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.tjohnn.popularmovieskotlin.R
import com.tjohnn.popularmovieskotlin.data.dto.ArrayResponse
import com.tjohnn.popularmovieskotlin.data.dto.Movie
import com.tjohnn.popularmovieskotlin.data.repository.LocalMovieRepository
import com.tjohnn.popularmovieskotlin.data.repository.MoviesRepository
import com.tjohnn.popularmovieskotlin.util.AppSchedulers
import com.tjohnn.popularmovieskotlin.util.Constants
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.*
import java.util.*

class MoviesViewModelTest{

    @Mock private lateinit var context: Application
    @Mock private lateinit var repository: MoviesRepository
    @Mock private lateinit var localRepository: LocalMovieRepository
    @Mock private lateinit var appSchedulers: AppSchedulers

    // Executes each mutable livedata task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MoviesViewModel

    @Captor
    private lateinit var moviesCaptor: ArgumentCaptor<MutableList<Movie>>

    // loading indicator observer
    @Mock lateinit var observer: Observer<Boolean>

    // endless loading indicator observer
    @Mock lateinit var endlessLoadObserver: Observer<Boolean>

    val emptyListMessage = "Empty movie list"

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = MoviesViewModel(context, repository, localRepository, appSchedulers)

        `when`(appSchedulers.io()).thenReturn(Schedulers.trampoline())
        `when`(appSchedulers.computation()).thenReturn(Schedulers.trampoline())
        `when`(appSchedulers.main()).thenReturn(Schedulers.trampoline())

        viewModel.isLoading.observeForever(observer)

        viewModel.isLoadingMore.observeForever(endlessLoadObserver)

        `when`(context.getString(R.string.empty_movie_list)).thenReturn(emptyListMessage)
    }

    @Test
    fun loadFavoriteMovies_shouldLoadData() {
        val movieList: List<Movie> = Arrays.asList(Movie(), Movie())
        val mock: Flowable<List<Movie>> = Flowable.fromArray(movieList)
        `when`(localRepository.getFavoriteMovies())
                .thenReturn(mock)
        with(viewModel){
            loadFavoriteMovies()

            verify(localRepository).getFavoriteMovies()

            verify(observer).onChanged(true)
            verify(observer).onChanged(false)

            assertTrue(movies.value?.size == 2)
        }
    }

    @Test
    fun loadFavoriteMovies_shouldLoadEmptyData() {
        val movieList: List<Movie> = arrayListOf()
        val mock: Flowable<List<Movie>> = Flowable.fromArray(movieList)
        `when`(localRepository.getFavoriteMovies())
                .thenReturn(mock)
        with(viewModel){
            loadFavoriteMovies()

            verify(localRepository).getFavoriteMovies()

            verify(observer).onChanged(true)
            verify(observer).onChanged(false)

            assertTrue(movies.value?.size == 0)

            assertTrue(pageErrorMessage.value == emptyListMessage)
        }
    }

    @Test
    fun loadMovies_shouldLoadData() {
        val movieList: List<Movie> = Arrays.asList(Movie(), Movie())
        val mock: Single<ArrayResponse<Movie>> = Single.just(ArrayResponse(1, 2, 1, movieList))
        `when`(repository.getMoviesByPopularity(ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyInt()))
                .thenReturn(mock)

        with(viewModel){
            loadMovies(Constants.BY_POPULARITY_VALUE, true, true)

            verify(repository).getMoviesByPopularity(true, 1)

            verify(observer).onChanged(true)
            verify(observer).onChanged(false)

            assertTrue(movies.value?.size == 2)
        }
    }

    @Test
    fun loadMoreMovies_shouldLoadData() {
        val movieList: MutableList<Movie> = arrayListOf(Movie(), Movie())
        val mock: Single<ArrayResponse<Movie>> = Single.just(ArrayResponse(2, 2, 1, movieList))
        `when`(repository.getMoviesByPopularity(ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyInt()))
                .thenReturn(mock)

        with(viewModel){
            viewModel.currentPage = 1
            totalPages = 3
            // add some data to movies since we are loading page 2 or higher
            movies = MutableLiveData()
            movies.postValue(movieList)

            loadNextPage(Constants.BY_POPULARITY_VALUE)

            assertTrue(currentPage == 2)

            verify(repository).getMoviesByPopularity(true, currentPage)

            verify(endlessLoadObserver).onChanged(true)
            verify(endlessLoadObserver).onChanged(false)

            assertTrue(movies.value?.size == 4)
        }
    }

    @Test
    fun onMovieItemClicked_shouldOpenDetail() {
        val MOVIE_ID = 1L
        with(viewModel){
            movieItemClicked(MOVIE_ID)

            assertTrue(openMovieDetail.value?.getContentIfNotHandled() == MOVIE_ID)

            assertFalse(openMovieDetail.value?.getContentIfNotHandled() == MOVIE_ID)
        }
    }
}