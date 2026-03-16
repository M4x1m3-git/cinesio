package com.example.cinesio.data.repository

import com.example.cinesio.data.local.dao.FriendDao
import com.example.cinesio.data.local.entity.FriendEntity

class FriendRepository(
    private val dao: FriendDao
) {

    /**
     * Ajouter ou remplacer un ami
     */
    suspend fun insert(friend: FriendEntity) {
        dao.insert(friend)
    }

    /**
     * Récupérer tous les amis
     */
    suspend fun getAllFriends(): List<FriendEntity>? {
        return dao.getAllFriends()
    }

    /**
     * Récupérer un ami par son id
     */
    suspend fun getById(id: Int): FriendEntity? {
        return dao.getById(id)
    }

    /**
     * Mettre à jour une relation d'amitié
     */
    suspend fun update(friend: FriendEntity) {
        dao.update(friend)
    }

    /**
     * Supprimer une relation d'amitié
     */
    suspend fun delete(friend: FriendEntity) {
        dao.delete(friend)
    }

    /**
     * Supprimer toutes les relations
     */
    suspend fun clear() {
        dao.clear()
    }
}