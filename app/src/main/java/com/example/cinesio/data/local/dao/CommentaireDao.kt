package com.example.cinesio.data.local.dao

import androidx.room.*
import com.example.cinesio.data.local.entity.CommentaireEntity

@Dao
interface CommentaireDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(commentaire: CommentaireEntity)

    @Query("SELECT * FROM commentaires WHERE id = :id")
    suspend fun getById(id: Int): CommentaireEntity?

    @Query("SELECT * FROM commentaires WHERE userFilmId = :userFilmId")
    suspend fun getByUserFilmId(userFilmId: Int): List<CommentaireEntity>

    @Update
    suspend fun update(commentaire: CommentaireEntity)

    @Delete
    suspend fun delete(commentaire: CommentaireEntity)

    @Query("DELETE FROM commentaires")
    suspend fun clear()
}