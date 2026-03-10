package com.example.cinesio.data.repository

import com.example.cinesio.api.MovieApi
import com.example.cinesio.data.model.Movie
import kotlinx.serialization.Serializable
import java.io.IOException

@Serializable
data class MoviesResponse(
    val results: List<Movie>
)


class MovieRepository(
    private val api: MovieApi
) {

    suspend fun fetchMovies(): ApiResult<List<Movie>> {

        return try {

            val response = api.getNowPlayingMovies()

            ApiResult.Success(response.results)

        } catch (e: IOException) {

            ApiResult.Error("Erreur réseau", e)

        } catch (e: Exception) {
            e.printStackTrace()
            ApiResult.Error("Erreur inattendue", e)

        }
    }
}