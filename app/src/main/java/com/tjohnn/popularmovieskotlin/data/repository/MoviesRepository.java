package com.tjohnn.popularmovieskotlin.data.repository;


import com.tjohnn.popularmovieskotlin.data.dto.ArrayResponse;
import com.tjohnn.popularmovieskotlin.data.dto.Movie;
import com.tjohnn.popularmovieskotlin.data.dto.Review;
import com.tjohnn.popularmovieskotlin.data.dto.Trailer;
import com.tjohnn.popularmovieskotlin.data.remote.ApiService;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

@Singleton
public class MoviesRepository {

    private ApiService apiService;

    @Inject
    public MoviesRepository(ApiService apiService){

        this.apiService = apiService;
    }

    public Single<ArrayResponse<Movie>> getMoviesByPopularity(boolean forceRefresh, int page){
        return apiService.getMoviesByRating(forceRefresh ? "no-cache" : null, page);
    }

    public Single<ArrayResponse<Movie>> getMoviesByRating(boolean forceRefresh, int page) {
        return apiService.getMoviesByPopularity(forceRefresh ? "no-cache" : null, page);
    }

    public Single<Movie> getMovieById(String movieId) {
        return apiService.getMovieById(movieId);
    }

    public Single<ArrayResponse<Trailer>> getMovieTrailers(String movieId) {
        return apiService.getMovieTrailers(movieId);
    }
    public Single<ArrayResponse<Review>> getMovieReviews(String movieId) {
        return apiService.getMovieReviews(movieId);
    }
}
