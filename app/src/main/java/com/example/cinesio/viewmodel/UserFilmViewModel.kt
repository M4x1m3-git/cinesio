package com.example.cinesio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinesio.data.local.entity.UserFilmEntity
import com.example.cinesio.data.repository.UserFilmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserFilmViewModel(
    private val repository: UserFilmRepository
) : ViewModel() {

    private val _userFilms = MutableStateFlow<List<UserFilmEntity>>(emptyList())
    val userFilms: StateFlow<List<UserFilmEntity>> = _userFilms

    fun loadUserFilms(userId: Int) {
        viewModelScope.launch {
            _userFilms.value = repository.getByUserId(userId)
        }
    }

    fun saveMovie(userId: Int, tmdbId: Int) {
        viewModelScope.launch {
            val userFilm = UserFilmEntity(
                userId = userId,
                tmdbId = tmdbId,
                saved = true
            )
            repository.insert(userFilm)
            loadUserFilms(userId)
        }
    }

    fun removeMovie(userFilm: UserFilmEntity, userId: Int) {
        viewModelScope.launch {
            repository.delete(userFilm)
            loadUserFilms(userId)
        }
    }
}