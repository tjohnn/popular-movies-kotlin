package com.tjohnn.popularmovieskotlin.data.remote


import com.tjohnn.popularmovieskotlin.data.dto.ArrayResponse
import com.tjohnn.popularmovieskotlin.data.dto.Movie
import com.tjohnn.popularmovieskotlin.data.dto.Review
import com.tjohnn.popularmovieskotlin.data.dto.Trailer

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("top_rated")
    fun getMoviesByRating(@Header("Cache-Control") cacheRule: String, @Query("page") page: Int): Single<ArrayResponse<Movie>>

    @GET("popular")
    fun getMoviesByPopularity(@Header("Cache-Control") cacheRule: String, @Query("page") page: Int): Single<ArrayResponse<Movie>>

    @GET("{movieId}")
    fun getMovieById(@Path("movieId") movieId: String): Single<Movie>


    @GET("{movieId}/reviews")
    fun getMovieReviews(@Path("movieId") movieId: String): Single<ArrayResponse<Review>>

    @GET("{movieId}/videos")
    fun getMovieTrailers(@Path("movieId") movieId: String): Single<ArrayResponse<Trailer>>
}
