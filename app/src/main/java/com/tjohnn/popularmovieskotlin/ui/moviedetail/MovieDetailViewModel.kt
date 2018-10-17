package com.tjohnn.popularmovieskotlin.ui.moviedetail

import android.app.Application
import android.arch.core.util.Function
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.tjohnn.popularmovieskotlin.data.dto.Movie
import com.tjohnn.popularmovieskotlin.data.dto.Review
import com.tjohnn.popularmovieskotlin.data.dto.Trailer
import com.tjohnn.popularmovieskotlin.data.local.FavoriteMovie
import com.tjohnn.popularmovieskotlin.data.repository.LocalMovieRepository
import com.tjohnn.popularmovieskotlin.data.repository.MoviesRepository
import com.tjohnn.popularmovieskotlin.util.AppSchedulers
import com.tjohnn.popularmovieskotlin.util.Utils.processNetworkError
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
        application: Application,
        val localMovieRepository: LocalMovieRepository,
        val movieRepository: MoviesRepository,
        private var appSchedulers: AppSchedulers
): AndroidViewModel(application) {


    var compositeDisposable: CompositeDisposable = CompositeDisposable()
    var movieId: Long = 0
    var movieLiveData: MutableLiveData<Movie> = MutableLiveData()
    var showLoadingIndicator: MutableLiveData<Boolean> = MutableLiveData()
    var pageMessage: MutableLiveData<String> = MutableLiveData()
    var trailersLiveData: MutableLiveData<List<Trailer>> = MutableLiveData()
    var reviewsLiveData: MutableLiveData<List<Review>> = MutableLiveData()
    var isFavourite: LiveData<Boolean> = MutableLiveData()

    init {
        trailersLiveData.value = arrayListOf()
        reviewsLiveData.value = arrayListOf()
    }

    fun loadMovieDetail(movieId: Long){
        isFavourite = Transformations.map(localMovieRepository.getFavoriteMovieById(movieId)) {
            it != null
        }


        if(this.movieId <= 0) {
            this.movieId = movieId
        }
        else return

        compositeDisposable.add(movieRepository.getMovieById(movieId)
                .doOnSubscribe{showLoadingIndicator.postValue(true)}
                .doAfterSuccess{showLoadingIndicator.postValue(false)}
                .doOnError{showLoadingIndicator.postValue(false)}
                .subscribeOn(appSchedulers.io())
                .observeOn(appSchedulers.main())
                .subscribe({
                    movieLiveData.postValue(it)
                    Timber.d(it.toString())
                }, { handleError(it) })
        )

        loadMovieTrailers()
        loadMovieReviews()
    }

    fun updateFavoriteMovie() {
        isFavourite.value?.let {
            movieLiveData.value?.let {
                Timber.d("I see ya, updating favorite movie")
                if(!isFavourite.value!!) {
                    compositeDisposable.addAll(localMovieRepository.createFavoriteMovie(FavoriteMovie(movieLiveData.value!!))
                            .subscribeOn(appSchedulers.io())
                            .observeOn(appSchedulers.main())
                            .subscribe()
                    )
                }
                else {
                    compositeDisposable.addAll(localMovieRepository.deleteFavoriteMovie(FavoriteMovie(movieLiveData.value!!))
                            .subscribeOn(appSchedulers.io())
                            .observeOn(appSchedulers.main())
                            .subscribe()
                    )
                }


            }
        }
    }

    private fun loadMovieTrailers() {
        compositeDisposable.add(movieRepository.getMovieTrailers(movieId)
                .subscribeOn(appSchedulers.io())
                .observeOn(appSchedulers.main())
                .subscribe({
                    trailersLiveData.postValue(it.results)
                }, { handleError(it) })
        )
    }

    private fun loadMovieReviews() {
        compositeDisposable.add(movieRepository.getMovieReviews(movieId)
                .subscribeOn(appSchedulers.io())
                .observeOn(appSchedulers.main())
                .subscribe({
                    reviewsLiveData.postValue(it.results)
                }, { handleError(it) })
        )
    }


    private fun handleError(throwable: Throwable) {
        pageMessage.postValue(processNetworkError(throwable))
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}