package com.example.flixyConnect.data.repository

import com.example.flixyConnect.api.MovieApi
import com.example.flixyConnect.data.model.Genre
import kotlinx.serialization.Serializable
import java.io.IOException

@Serializable
data class GenresResponse(
    val genres: List<Genre>
)

class GenreRepository(
    private val api: MovieApi
) {

    suspend fun fetchGenres(): ApiResult<List<Genre>> {

        return try {

            val response = api.getMovieGenres()

            ApiResult.Success(response.genres)

        } catch (e: IOException) {

            ApiResult.Error("Erreur réseau", e)

        } catch (e: Exception) {

            e.printStackTrace()
            ApiResult.Error("Erreur inattendue", e)

        }
    }
}
