package com.example.cinesio.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinesio.data.local.database.AppDatabase
import com.example.cinesio.data.local.entity.UserEntity
import com.example.cinesio.data.local.entity.CommentaireEntity
import com.example.cinesio.data.local.entity.UserFilmEntity
import com.example.cinesio.data.repository.CommentaireRepository
import com.example.cinesio.data.repository.UserFilmRepository
import com.example.cinesio.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository
    private val commentaireRepository: CommentaireRepository
    private val userFilmRepository: UserFilmRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = UserRepository(db.userDao())
        commentaireRepository = CommentaireRepository(db.commentaireDao())
        userFilmRepository = UserFilmRepository(db.userFilmDao())
    }

    private val _users = MutableStateFlow<List<UserEntity>>(emptyList())
    val users: StateFlow<List<UserEntity>> = _users

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser

    private val _reviewCount = MutableStateFlow(0)
    val reviewCount: StateFlow<Int> = _reviewCount

    private val _savedMoviesCount = MutableStateFlow(0)
    val savedMoviesCount: StateFlow<Int> = _savedMoviesCount

    private val _comments = MutableStateFlow<List<CommentaireEntity>>(emptyList())
    val comments: StateFlow<List<CommentaireEntity>> = _comments

    private val _userFilms = MutableStateFlow<List<UserFilmEntity>>(emptyList())
    val userFilms: StateFlow<List<UserFilmEntity>> = _userFilms

    fun loadUsers() {
        viewModelScope.launch {
            _users.value = repository.getAllUsers()
        }
    }

    fun loadUser(id: Int) {
        viewModelScope.launch {
            _currentUser.value = repository.getUserById(id)
            loadStats(id)
        }
    }

    fun register(user: UserEntity, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val exists = repository.isEmailTaken(user.email)

            if (exists) {
                onResult(false)
            } else {
                val hashedPassword = BCrypt.hashpw(user.password, BCrypt.gensalt())

                val secureUser = user.copy(password = hashedPassword)

                val id = repository.saveUser(secureUser)

                val sharedPref = getApplication<Application>()
                    .getSharedPreferences("prefs", android.content.Context.MODE_PRIVATE)

                with(sharedPref.edit()) {
                    putString("email", user.email)
                    putString("username", user.username)
                    putInt("userId", id.toInt())
                    apply()
                }

                _currentUser.value = secureUser.copy(id = id.toInt())
                onResult(true)
            }
        }
    }

    fun deleteUser(user: UserEntity) {
        viewModelScope.launch {
            repository.deleteUser(user)
            loadUsers()
        }
    }

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUserByEmail(email)

            if (user != null && BCrypt.checkpw(password, user.password)) {
                _currentUser.value = user
                loadStats(user.id)
                onResult(true)
                val sharedPref = getApplication<Application>()
                    .getSharedPreferences("prefs", android.content.Context.MODE_PRIVATE)

                with(sharedPref.edit()) {
                    putString("email", user.email)
                    putString("username", user.username)
                    putInt("userId", user.id)
                    apply()
                }
            } else {
                _currentUser.value = null
                onResult(false)
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _reviewCount.value = 0
        _savedMoviesCount.value = 0
        _comments.value = emptyList()
    }

    fun loadStats(userId: Int) {
        viewModelScope.launch {
            _reviewCount.value = commentaireRepository.getReviewCount(userId)
            _savedMoviesCount.value = userFilmRepository.getSavedMoviesCount(userId)
            _comments.value = commentaireRepository.getUserCommentaires(userId)
            _userFilms.value = userFilmRepository.getByUserId(userId)
        }
    }

    fun deleteComment(comment: CommentaireEntity, userId: Int) {
        viewModelScope.launch {
            commentaireRepository.delete(comment)
            loadStats(userId)
        }
    }
}