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

    fun addCommentaire(commentaire: CommentaireEntity) {
        viewModelScope.launch {
            repository.insert(commentaire)
            loadCommentaires(commentaire.userFilmId)
        }
    }

    fun updateCommentaire(commentaire: CommentaireEntity) {
        viewModelScope.launch {
            repository.update(commentaire)
            loadCommentaires(commentaire.userFilmId)
        }
    }

    fun deleteCommentaire(commentaire: CommentaireEntity) {
        viewModelScope.launch {
            repository.delete(commentaire)
            loadCommentaires(commentaire.userFilmId)
        }
    }

    fun clearCommentaires() {
        viewModelScope.launch {
            repository.clear()
            _commentaires.value = emptyList()
        }
    }
}