package com.example.cinesio.data.repository

import com.example.cinesio.api.MovieApi
import com.example.cinesio.data.model.Video
import kotlinx.serialization.Serializable
import java.io.IOException

@Serializable
data class VideoResponse (
    val results: List<Video>
)

class VideoRepository(
    private val api: MovieApi
) {

    suspend fun fetchVideos(movieId: Int): ApiResult<List<Video>> {
        return try {

            val response = api.getMovieVideos(movieId)

            ApiResult.Success(response.results)

        } catch (e: IOException) {

            ApiResult.Error("Erreur réseau", e)

        } catch (e: Exception) {

            e.printStackTrace()
            ApiResult.Error("Erreur inattendue", e)

        }
    }
}