package com.example.cinesio.data.repository

import com.example.cinesio.data.local.dao.CommentaireDao
import com.example.cinesio.data.local.entity.CommentaireEntity

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
}