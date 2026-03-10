package com.example.cinesio.api

import com.example.cinesio.data.repository.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("3/movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("language") language: String = "fr-FR",
        @Query("region") region: String = "FR",
        @Query("page") page: Int = 1
    ): MoviesResponse
}