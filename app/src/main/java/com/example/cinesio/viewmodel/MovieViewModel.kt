package com.example.cinesio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinesio.data.model.Genre
import com.example.cinesio.data.model.Movie
import com.example.cinesio.data.repository.ApiResult
import com.example.cinesio.data.repository.GenreRepository
import com.example.cinesio.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MovieUiState(
    val loading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val upcoming: List<Movie> = emptyList(),
    val genres: List<Genre> = emptyList(),
    val error: String? = null
) {
    val genreMap: Map<Int, String> get() = genres.associate { it.id to it.name }
}

class MovieViewModel(
    private val repo: MovieRepository,
    private val repoGenre: GenreRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MovieUiState())
    val state: StateFlow<MovieUiState> = _state

    init {
        loadGenres()
        loadMovies()
        loadUpcoming()
    }

    /**
     * Charge uniquement les films Now Playing depuis Room / API
     */
    fun loadMovies() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            try {
                val moviesResult = repo.fetchMovies() // fetchMovies() → now_playing
                when (moviesResult) {
                    is ApiResult.Success -> _state.update { it.copy(movies = moviesResult.data, loading = false) }
                    is ApiResult.Error -> _state.update { it.copy(error = moviesResult.message, loading = false) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(error = e.message, loading = false) }
            }
        }
    }

    /**
     * Charge uniquement les films à venir (Upcoming)
     */
    fun loadUpcoming() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            try {
                val upcomingResult = repo.fetchUpcoming() // fetchUpcoming() → upcoming
                when (upcomingResult) {
                    is ApiResult.Success -> _state.update { it.copy(upcoming = upcomingResult.data, loading = false) }
                    is ApiResult.Error -> _state.update { it.copy(error = upcomingResult.message, loading = false) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(error = e.message, loading = false) }
            }
        }
    }

    /**
     * Charge les genres depuis l'API
     */
    fun loadGenres() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            when (val result = repoGenre.fetchGenres()) {
                is ApiResult.Success -> _state.update { it.copy(genres = result.data, loading = false) }
                is ApiResult.Error -> _state.update { it.copy(error = result.message, loading = false) }
            }
        }
    }
}