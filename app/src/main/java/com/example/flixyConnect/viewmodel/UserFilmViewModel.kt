package com.example.flixyConnect.viewmodel

import android.content.Context
import android.provider.SyncStateContract.Helpers.insert
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flixyConnect.data.local.dao.UserFilmDao
import com.example.flixyConnect.data.local.entity.UserFilmEntity
import com.example.flixyConnect.data.model.Movie
import com.example.flixyConnect.data.repository.UserFilmRepository
import com.example.flixyConnect.utils.NotificationScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserFilmViewModel(
    private val repository: UserFilmRepository
) : ViewModel() {

    private val _userFilms = MutableStateFlow<List<UserFilmEntity>>(emptyList())
    val userFilms: StateFlow<List<UserFilmEntity>> = _userFilms
    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved

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

    fun toggleSaved(userId: Int, tmdbId: Int) {
        viewModelScope.launch {
            repository.toggleSaved(userId, tmdbId)
            loadUserFilms(userId)
        }
    }

    fun isSaved(userId: Int, tmdbId: Int) {
        viewModelScope.launch {
            _isSaved.value = repository.isSaved(userId, tmdbId)
        }
    }

    fun getOrCreateUserFilm(
        userId: Int,
        tmdbId: Int,
        onResult: (UserFilmEntity) -> Unit
    ) {
        viewModelScope.launch {
            val userFilm = repository.getOrCreate(userId, tmdbId)
            onResult(userFilm)
        }
    }

    fun updateRating(userFilmId: Int, rating: Float?) {
        viewModelScope.launch {
            repository.updateRating(userFilmId, rating)
        }
    }

    fun toggleNotification(
        context: Context,
        userFilm: UserFilmEntity,
        movie: Movie
    ) {
        viewModelScope.launch {

            val newValue = !userFilm.notifyOnRelease

            repository.setNotify(userFilm.id, newValue)

            if (newValue) {
                NotificationScheduler.schedule(
                    context,
                    movie.id,
                    movie.title,
                    movie.releaseDate
                )
            } else {
                NotificationScheduler.cancel(context, movie.id)
            }

            loadUserFilms(userFilm.userId)
        }
    }
}