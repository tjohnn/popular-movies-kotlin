package com.tjohnn.popularmovieskotlin.ui.movies

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.tjohnn.popularmovieskotlin.R
import com.tjohnn.popularmovieskotlin.data.dto.Movie
import com.tjohnn.popularmovieskotlin.data.repository.LocalMovieRepository
import com.tjohnn.popularmovieskotlin.data.repository.MoviesRepository
import com.tjohnn.popularmovieskotlin.util.Constants
import com.tjohnn.popularmovieskotlin.util.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class MoviesViewModel @Inject constructor(private val app: Application, private var movieRepository: MoviesRepository, private var localMovieRepository: LocalMovieRepository) : AndroidViewModel(app) {


    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    var isLoadingMore: MutableLiveData<Boolean> = MutableLiveData()
    var movies: MutableLiveData<MutableList<Movie>> = MutableLiveData()
    var pageErrorMessage: MutableLiveData<String> = MutableLiveData()
    var toastMessage: MutableLiveData<String> = MutableLiveData()
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var currentPage: Int = 0
    private var totalPages: Int = 0


    fun loadFavoriteMovies(){
        // clear previous observables to disallow multiple observables updating movies list
        compositeDisposable.clear()

        movies.value = mutableListOf()

        compositeDisposable.add(localMovieRepository.getFavoriteMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe{isLoading.value = true}
                .doAfterNext{isLoading.value = false}
                .doOnError{isLoading.value = false}
                .subscribe({
                    movies.value?.addAll(it)
                    val list = movies.value ?: arrayListOf()
                    list.addAll(it as Iterable<Movie>)
                    updateMovies(list)

                }, { pageErrorMessage.value = it.message}, {}))
    }

    fun loadMovies(sortType: String, forceRefresh: Boolean, isInitialCall: Boolean){
        // do not reload if data is present, this is to take care of orientation change
        if(isInitialCall && movies.value != null && movies.value!!.size > 0) return

        // clear previous observables to disallow multiple observables updating movies list
        compositeDisposable.clear()

        currentPage = 1
        movies.value = arrayListOf()
        getMovies(sortType, forceRefresh, currentPage)
    }

    fun loadNextPage(sortType: String){
        currentPage++
        if(currentPage > totalPages) return
        // always reload other pages
        getMovies(sortType, true, currentPage)

    }


    private fun getMovies(sortType: String, forceRefresh: Boolean, page: Int) {
        compositeDisposable.add(when(sortType) {
            Constants.BY_POPULARITY_VALUE -> movieRepository.getMoviesByPopularity(forceRefresh, page)
            else -> movieRepository.getMoviesByRating(forceRefresh, page)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe{when(page){
                    1 -> isLoading.value = true
                    else -> isLoadingMore.value = true
                }}
                .doAfterSuccess{when(page){
                    1 -> isLoading.value = false
                    else -> isLoadingMore.value = false
                }}
                .doOnError{when(page){
                    1 -> isLoading.value = false
                    else -> isLoadingMore.value = false
                }}
                .subscribe({
                    currentPage = it.page
                    totalPages = it.totalPages
                    val list = movies.value ?: arrayListOf()
                    list.addAll(it.results as Iterable<Movie>)
                    updateMovies(list)
                    Timber.d(movies.value.toString())

                }, {
                    when (currentPage) {
                        1 -> pageErrorMessage.value = Utils.processNetworkError(it)
                        else -> toastMessage.value = Utils.processNetworkError(it)
                    }
                }))

    }

    private fun updateMovies(list: MutableList<Movie>) {
        movies.postValue(list)
        if((movies.value?.size ?: 0) < 1){
            pageErrorMessage.value = app.getString(R.string.empty_movie_list)
        }
        else {
            pageErrorMessage.value = null
        }
    }


    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}