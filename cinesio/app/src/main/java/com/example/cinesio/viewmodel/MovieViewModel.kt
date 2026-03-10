package com.example.cinesio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinesio.data.model.Movie
import com.example.cinesio.data.repository.ApiResult
import com.example.cinesio.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class MovieUiState(
    val loading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: String? = null
)

class MovieViewModel(
    private val repo: MovieRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MovieUiState())
    val state: StateFlow<MovieUiState> = _state

    fun loadMovies() {

        viewModelScope.launch {

            _state.value = MovieUiState(loading = true)

            val result = repo.fetchMovies()

            _state.value = when (result) {

                is ApiResult.Success ->
                    MovieUiState(movies = result.data)

                is ApiResult.Error ->
                    MovieUiState(error = result.message)
            }
        }
    }
}