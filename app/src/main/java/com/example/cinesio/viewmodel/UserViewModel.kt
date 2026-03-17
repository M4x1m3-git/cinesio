package com.example.cinesio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinesio.data.local.entity.UserEntity
import com.example.cinesio.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {

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
            loadUsers()
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
}