package com.example.cinesio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val username: String,
    val email: String,
    val roles: String, // JSON ou CSV si tu veux stocker plusieurs rôles
    val avatar: String? = null
)