package com.example.cinesio.data.repository

import android.util.Log
import com.example.cinesio.api.MovieApi
import com.example.cinesio.data.local.canRefresh
import com.example.cinesio.data.local.dao.MovieDao
import com.example.cinesio.data.local.entity.MovieEntity
import com.example.cinesio.data.model.Movie
import com.example.cinesio.data.model.MovieDetails
import kotlinx.serialization.Serializable
import java.io.IOException

@Serializable
data class MoviesResponse(
    val results: List<Movie>
)

class MovieRepository(
    private val api: MovieApi,
    private val dao: MovieDao
) {

    /**
     * Retourne la liste de tous les films (upcoming + now_playing)
     * Fait un refresh si nécessaire, sinon retourne la base locale
     */
    suspend fun getMovies(): List<MovieEntity> {
        val lastUpdate = dao.getLastUpdated()
        Log.d("MovieRepository", "Last update timestamp: $lastUpdate")

        if (canRefresh(lastUpdate)) {
            Log.d("MovieRepository", "Refreshing movies from API")
            refreshMovies()
        }

        val movies = dao.getAllMovies()
        Log.d("MovieRepository", "Number of movies in database: ${movies.size}")
        return movies
    }

    /**
     * Refresh complet : fetch API TMDB + insert en base locale
     */
    private suspend fun refreshMovies() {
        val upcomingResponse = api.getUpcoming()
        val nowPlayingResponse = api.getNowPlayingMovies()

        val allMovies = (upcomingResponse.results + nowPlayingResponse.results).map { movie ->
            MovieEntity(
                id = movie.id,
                title = movie.title,
                posterPath = movie.posterPath ?: "",
                releaseDate = movie.releaseDate,
                voteAverage = movie.voteAverage,
                voteCount = movie.voteCount ?: 0,
                genreIds = movie.genre_ids.joinToString(","),
                lastUpdated = System.currentTimeMillis()
            )
        }

        Log.d("MovieRepository", "Inserting ${allMovies.size} movies into database")
        dao.clearMovies()
        dao.insertMovies(allMovies)
    }

    /**
     * Transforme les entités locales en modèle ApiResult
     */
    suspend fun fetchMovies(): ApiResult<List<Movie>> {
        return try {
            val entities = getMovies()
            val movies = entities.map {
                Movie(
                    id = it.id,
                    title = it.title,
                    posterPath = it.posterPath,
                    releaseDate = it.releaseDate,
                    voteAverage = it.voteAverage,
                    voteCount = it.voteCount,
                    genre_ids = if (it.genreIds.isEmpty()) emptyList() else it.genreIds.split(",").map { s -> s.toInt() }
                )
            }
            ApiResult.Success(movies)
        } catch (e: Exception) {
            Log.e("MovieRepository", "Erreur récupération films", e)
            ApiResult.Error("Erreur récupération films", e)
        }
    }

    suspend fun fetchUpcoming(): ApiResult<List<Movie>> {
        return try {
            val response = api.getUpcoming()
            ApiResult.Success(response.results)
        } catch (e: IOException) {
            Log.e("MovieRepository", "Erreur réseau", e)
            ApiResult.Error("Erreur réseau", e)
        } catch (e: Exception) {
            Log.e("MovieRepository", "Erreur inattendue", e)
            ApiResult.Error("Erreur inattendue", e)
        }
    }

    suspend fun fetchMovieById(id: Int): ApiResult<MovieDetails> {
        return try {
            val movie = api.getMovieDetails(id)
            ApiResult.Success(movie)
        } catch (e: IOException) {
            Log.e("MovieRepository", "Erreur réseau", e)
            ApiResult.Error("Erreur réseau", e)
        } catch (e: Exception) {
            Log.e("MovieRepository", "Erreur inattendue", e)
            ApiResult.Error("Erreur inattendue", e)
        }
    }
}