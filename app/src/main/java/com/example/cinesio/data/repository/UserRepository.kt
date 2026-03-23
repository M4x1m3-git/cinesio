package com.example.cinesio.data.repository

import com.example.cinesio.data.local.dao.UserDao
import com.example.cinesio.data.local.entity.UserEntity

class UserRepository(
    private val dao: UserDao
) {

    /**
     * Insère ou met à jour un utilisateur en local
     */
    suspend fun saveUser(user: UserEntity): Long {
        return dao.insert(user)
    }

    /**
     * Récupère un utilisateur par son id
     */
    suspend fun getUserById(id: Int): UserEntity? {
        return dao.getUserById(id)
    }

    /**
     * Récupère tous les utilisateurs
     */
    suspend fun getAllUsers(): List<UserEntity> {
        return dao.getAllUsers()
    }

    /**
     * Supprime un utilisateur
     */
    suspend fun deleteUser(user: UserEntity) {
        dao.delete(user)
    }

    /**
     * Connexion de l'utilisateur
     * */
    suspend fun getUserByEmail(email: String): UserEntity? {
        return dao.getUserByEmail(email)
    }

    /**
     * Vérifier si l'email est déjà prise
     */
    suspend fun isEmailTaken(email: String): Boolean {
        return dao.getUserByEmail(email) != null
    }
}