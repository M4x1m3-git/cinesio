package com.example.flixyConnect.data.local.dao

import androidx.room.*
import com.example.flixyConnect.data.local.entity.UserFilmEntity

@Dao
interface UserFilmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userFilm: UserFilmEntity)

    @Query("SELECT * FROM user_films WHERE tmdbId = :tmdbId AND userId = :userId")
    suspend fun getByMovieIdAndUser(tmdbId: Int, userId: Int): UserFilmEntity?

    @Query("SELECT * FROM user_films WHERE userId = :userId")
    suspend fun getByUserId(userId: Int): List<UserFilmEntity>

    @Query("SELECT * FROM user_films WHERE id = :id")
    suspend fun getById(id: Int): UserFilmEntity?

    @Update
    suspend fun update(userFilm: UserFilmEntity)

    @Delete
    suspend fun delete(userFilm: UserFilmEntity)

    @Query("DELETE FROM user_films")
    suspend fun clear()

    @Query("SELECT COUNT(*) FROM user_films WHERE userId = :userId AND saved = 1")
    suspend fun getSavedMoviesCount(userId: Int): Int

    @Query("UPDATE user_films SET rating = :rating WHERE id = :userFilmId")
    suspend fun updateRating(userFilmId: Int, rating: Float?)
}