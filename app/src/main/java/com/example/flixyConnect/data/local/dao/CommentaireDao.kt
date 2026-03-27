package com.example.flixyConnect.data.local.dao

import androidx.room.*
import com.example.flixyConnect.data.local.entity.CommentaireEntity

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

    @Query("""
        SELECT c.* FROM commentaires c
        INNER JOIN user_films uf ON c.userFilmId = uf.id
        WHERE uf.userId = :userId
        ORDER BY c.createdAt DESC
    """)
    suspend fun getCommentairesByUser(userId: Int): List<CommentaireEntity>

    @Query("""
        SELECT COUNT(*) FROM commentaires c
        INNER JOIN user_films uf ON c.userFilmId = uf.id
        WHERE uf.userId = :userId
    """)
    suspend fun getReviewCount(userId: Int): Int

    @Query("""
    SELECT c.id, c.comment, c.createdAt, c.userFilmId, u.username
    FROM commentaires c
    INNER JOIN user_films uf ON c.userFilmId = uf.id
    INNER JOIN users u ON uf.userId = u.id
    WHERE uf.tmdbId = :tmdbId
    ORDER BY c.createdAt DESC
""")
    suspend fun getCommentairesByFilm(tmdbId: Int): List<CommentaireEntity>
}