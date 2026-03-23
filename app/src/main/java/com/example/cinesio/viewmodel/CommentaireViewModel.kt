package com.example.cinesio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinesio.data.local.entity.CommentaireEntity
import com.example.cinesio.data.repository.CommentaireRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CommentaireViewModel(
    private val repository: CommentaireRepository
) : ViewModel() {

    private val _commentaires = MutableStateFlow<List<CommentaireEntity>>(emptyList())
    val commentaires: StateFlow<List<CommentaireEntity>> = _commentaires

    fun loadCommentaires(userFilmId: Int) {
        viewModelScope.launch {
            _commentaires.value = repository.getByUserFilmId(userFilmId)
        }
    }

    fun addCommentaire(commentaire: CommentaireEntity, filmId: Int) {
        viewModelScope.launch {
            repository.insert(commentaire)
            loadCommentairesByFilm(filmId)
        }
    }

    fun updateCommentaire(commentaire: CommentaireEntity, filmId: Int) {
        viewModelScope.launch {
            repository.update(commentaire)
            loadCommentairesByFilm(filmId)
        }
    }

    fun deleteCommentaire(commentaire: CommentaireEntity, filmId: Int) {
        viewModelScope.launch {
            repository.delete(commentaire)
            loadCommentairesByFilm(filmId)
        }
    }

    fun clearCommentaires() {
        viewModelScope.launch {
            repository.clear()
            _commentaires.value = emptyList()
        }
    }

    fun loadCommentairesByFilm(tmdbId: Int) {
        viewModelScope.launch {
            _commentaires.value = repository.getCommentairesByFilm(tmdbId)
        }
    }
}