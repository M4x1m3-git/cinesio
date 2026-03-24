package com.example.flixyConnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flixyConnect.data.model.MovieDetails
import com.example.flixyConnect.data.model.Video
import com.example.flixyConnect.data.repository.ApiResult
import com.example.flixyConnect.data.repository.MovieRepository
import com.example.flixyConnect.data.repository.VideoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MovieDetailUiState(
    val loading: Boolean = false,
    val movie: MovieDetails? = null,
    val videos: List<Video> = emptyList(),
    val error: String? = null
) {
    val trailer: Video?
        get() = videos.firstOrNull { it.site == "YouTube" }
}

class MovieDetailViewModel(
    private val repoMovie: MovieRepository,
    private val repoVideo: VideoRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MovieDetailUiState())
    val state: StateFlow<MovieDetailUiState> = _state.asStateFlow()

    fun loadMovieDetails(movieId: Int) {

        viewModelScope.launch {

            // UI -> loading
            _state.update { it.copy(loading = true, error = null) }

            val movieResult = repoMovie.fetchMovieById(movieId)
            val videoResult = repoVideo.fetchVideos(movieId)

            val movie = when (movieResult) {
                is ApiResult.Success -> movieResult.data
                is ApiResult.Error -> null
            }

            val videos = when (videoResult) {
                is ApiResult.Success -> videoResult.data
                is ApiResult.Error -> emptyList()
            }

            val errorMessage =
                (movieResult as? ApiResult.Error)?.message
                    ?: (videoResult as? ApiResult.Error)?.message

            _state.update {
                it.copy(
                    loading = false,
                    movie = movie,
                    videos = videos,
                    error = errorMessage
                )
            }
        }
    }
}