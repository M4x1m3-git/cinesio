package com.example.flixyConnect.data.repository

import com.example.flixyConnect.data.local.dao.CommentaireDao
import com.example.flixyConnect.data.local.entity.CommentaireEntity

class CommentaireRepository(
    private val dao: CommentaireDao
) {

    /**
     * Ajouter ou remplacer un commentaire
     */
    suspend fun insert(commentaire: CommentaireEntity) {
        dao.insert(commentaire)
    }

    /**
     * Récupérer un commentaire par son id
     */
    suspend fun getById(id: Int): CommentaireEntity? {
        return dao.getById(id)
    }

    /**
     * Récupérer tous les commentaires liés à un film utilisateur
     */
    suspend fun getByUserFilmId(userFilmId: Int): List<CommentaireEntity> {
        return dao.getByUserFilmId(userFilmId)
    }

    /**
     * Mettre à jour un commentaire
     */
    suspend fun update(commentaire: CommentaireEntity) {
        dao.update(commentaire)
    }

    /**
     * Supprimer un commentaire
     */
    suspend fun delete(commentaire: CommentaireEntity) {
        dao.delete(commentaire)
    }

    /**
     * Supprimer tous les commentaires
     */
    suspend fun clear() {
        dao.clear()
    }

    /**
     * Récuperer les commentaires d'un utilisateur
     * */
    suspend fun getUserCommentaires(userId: Int): List<CommentaireEntity> {
        return dao.getCommentairesByUser(userId)
    }

    /**
     * Récuperer le nombre de review d'un utilisateur
     */
    suspend fun getReviewCount(userId: Int): Int {
        return dao.getReviewCount(userId)
    }

    /**
     * Récuperer les commentaires à partir d'un film
     */
    suspend fun getCommentairesByFilm(tmdbId: Int): List<CommentaireEntity> {
        return dao.getCommentairesByFilm(tmdbId)
    }
}