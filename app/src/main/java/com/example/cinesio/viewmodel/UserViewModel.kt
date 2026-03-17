package com.example.cinesio.viewmodel

import android.app.Application
import android.provider.Settings.Global.putString
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinesio.data.local.database.AppDatabase
import com.example.cinesio.data.local.entity.UserEntity
import com.example.cinesio.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = UserRepository(db.userDao())
    }

    private val _users = MutableStateFlow<List<UserEntity>>(emptyList())
    val users: StateFlow<List<UserEntity>> = _users

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser

    fun loadUsers() {
        viewModelScope.launch {
            _users.value = repository.getAllUsers()
        }
    }

    fun loadUser(id: Int) {
        viewModelScope.launch {
            _currentUser.value = repository.getUserById(id)
        }
    }

    fun saveUser(user: UserEntity) {
        viewModelScope.launch {
            repository.saveUser(user)

            val sharedPref = getApplication<Application>().getSharedPreferences("prefs", android.content.Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("email", user.email)
                putString("username", user.username)
                apply()
            }
            _currentUser.value = user
        }
    }

    fun deleteUser(user: UserEntity) {
        viewModelScope.launch {
            repository.deleteUser(user)
            loadUsers()
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val user = repository.login(email, password)
            _currentUser.value = user
        }
    }

    fun logout() {
        _currentUser.value = null
    }
}