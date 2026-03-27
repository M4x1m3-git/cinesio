package com.example.flixyConnect.api

import com.example.flixyConnect.data.model.MovieDetails
import com.example.flixyConnect.data.repository.GenresResponse
import com.example.flixyConnect.data.repository.MoviesResponse
import com.example.flixyConnect.data.repository.VideoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("3/movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("language") language: String = "fr-FR",
        @Query("region") region: String = "FR",
        @Query("page") page: Int = 1
    ): MoviesResponse

    @GET("3/genre/movie/list")
    suspend fun getMovieGenres(
        @Query("language") language: String = "fr-FR"
    ): GenresResponse

    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetails(
        @retrofit2.http.Path("movie_id") movieId: Int,
        @Query("language") language: String = "fr-FR"
    ): MovieDetails

//    @GET("3/movie/{movie_id}/credits")
//    suspend fun getMovieCredits(
//        @retrofit2.http.Path("movie_id") movieId: Int,
//        @Query("language") language: String = "fr-FR"
//    ): MovieCreditsResponse

    @GET("3/movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @retrofit2.http.Path("movie_id") movieId: Int,
        @Query("language") language: String = "fr-FR"
    ): VideoResponse

    @GET("3/movie/upcoming")
    suspend fun getUpcoming(
        @Query("language") language: String = "fr-FR",
        @Query("region") region: String = "FR",
        @Query("sort_by") sortBy: String = "primary_release_date.asc",
        @Query("page") page: Int = 1
    ): MoviesResponse
}
