package com.example.cinesio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinesio.data.local.entity.FriendEntity
import com.example.cinesio.data.repository.FriendRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FriendViewModel(
    private val repository: FriendRepository
) : ViewModel() {

    private val _friends = MutableStateFlow<List<FriendEntity>>(emptyList())
    val friends: StateFlow<List<FriendEntity>> = _friends

    fun loadFriends() {
        viewModelScope.launch {
            _friends.value = repository.getAllFriends() ?: emptyList()
        }
    }

    fun addFriend(friend: FriendEntity) {
        viewModelScope.launch {
            repository.insert(friend)
            loadFriends()
        }
    }

    fun updateFriend(friend: FriendEntity) {
        viewModelScope.launch {
            repository.update(friend)
            loadFriends()
        }
    }

    fun deleteFriend(friend: FriendEntity) {
        viewModelScope.launch {
            repository.delete(friend)
            loadFriends()
        }
    }

    fun clearFriends() {
        viewModelScope.launch {
            repository.clear()
            _friends.value = emptyList()
        }
    }
}