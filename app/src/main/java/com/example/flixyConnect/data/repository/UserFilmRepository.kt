package com.example.flixyConnect.data.repository

import com.example.flixyConnect.data.local.dao.UserFilmDao
import com.example.flixyConnect.data.local.entity.UserFilmEntity

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
     * Récupère tous les films d’un utilisateur
     */
    suspend fun getByUserId(userId: Int): List<UserFilmEntity> {
        return dao.getByUserId(userId)
    }

    /**
     * Récupère toutes les infos films d’un film
     */
    suspend fun getById(id: Int): UserFilmEntity? {
        return dao.getById(id)
    }

    /**
     * Vérifie si un film est déjà sauvegardé
     */
    suspend fun isMovieSaved(tmdbId: Int, userId: Int): Boolean {
        return dao.getByMovieIdAndUser(tmdbId, userId) != null
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

    /**
     * Récuperer le nombre de films enregistré
     */
    suspend fun getSavedMoviesCount(userId: Int): Int {
        return dao.getSavedMoviesCount(userId)
    }

    /**
     * Sauvegarder ou non un film pour un utilisateur
     */
    suspend fun toggleSaved(userId: Int, tmdbId: Int) {
        val existing = dao.getByMovieIdAndUser(tmdbId, userId)

        if (existing == null) {
            dao.insert(
                UserFilmEntity(
                    userId = userId,
                    tmdbId = tmdbId,
                    saved = true
                )
            )
        } else {
            dao.update(
                existing.copy(saved = !existing.saved)
            )
        }
    }

    /**
     * Vérifier si un film est sauvegardé
     */
    suspend fun isSaved(userId: Int, tmdbId: Int): Boolean {
        return dao.getByMovieIdAndUser(tmdbId, userId)?.saved == true
    }

    /**
     * Créer ou récuperer un userFilm
     */
    suspend fun getOrCreate(userId: Int, tmdbId: Int): UserFilmEntity {
        val existing = dao.getByMovieIdAndUser(tmdbId, userId)

        return if (existing != null) {
            existing
        } else {
            val newUserFilm = UserFilmEntity(
                userId = userId,
                tmdbId = tmdbId,
                saved = false
            )
            dao.insert(newUserFilm)
            dao.getByMovieIdAndUser(tmdbId, userId)!!
        }
    }

    /**
     * Modifier une notation
     */
    suspend fun updateRating(userFilmId: Int, rating: Float?) {
        dao.updateRating(userFilmId, rating)
    }

    /**
     * Définir une nouvelle notification
     */
    suspend fun setNotify(userFilmId: Int, enabled: Boolean) {
        val film = dao.getById(userFilmId) ?: return
        dao.update(film.copy(notifyOnRelease = enabled))
    }
}