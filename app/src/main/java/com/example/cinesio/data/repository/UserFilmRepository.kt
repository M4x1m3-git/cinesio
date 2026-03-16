package com.example.cinesio.data.repository

import com.example.cinesio.data.local.dao.UserFilmDao
import com.example.cinesio.data.local.entity.UserFilmEntity

class UserFilmRepository(
    private val dao: UserFilmDao
) {

    /**
     * Insère ou remplace un film lié à un utilisateur
     */
    suspend fun insert(userFilm: UserFilmEntity) {
        dao.insert(userFilm)
    }

    /**
     * Récupère un film via son tmdbId
     */
    suspend fun getByMovieId(tmdbId: Int): UserFilmEntity? {
        return dao.getByMovieId(tmdbId)
    }

    /**
     * Récupère tous les films d’un utilisateur
     */
    suspend fun getByUserId(userId: Int): List<UserFilmEntity> {
        return dao.getByUserId(userId)
    }

    /**
     * Vérifie si un film est déjà sauvegardé
     */
    suspend fun isMovieSaved(tmdbId: Int): Boolean {
        return dao.getByMovieId(tmdbId) != null
    }

    /**
     * Met à jour un film utilisateur
     */
    suspend fun update(userFilm: UserFilmEntity) {
        dao.update(userFilm)
    }

    /**
     * Supprime un film utilisateur
     */
    suspend fun delete(userFilm: UserFilmEntity) {
        dao.delete(userFilm)
    }

    /**
     * Supprime tous les films sauvegardés
     */
    suspend fun clear() {
        dao.clear()
    }
}