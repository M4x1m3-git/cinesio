package com.example.cinesio.data.local.dao

import androidx.room.*
import com.example.cinesio.data.local.entity.UserFilmEntity

@Dao
interface UserFilmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userFilm: UserFilmEntity)

    @Query("SELECT * FROM user_films WHERE tmdbId = :tmdbId")
    suspend fun getByMovieId(tmdbId: Int): UserFilmEntity?

    @Query("SELECT * FROM user_films WHERE userId = :userId")
    suspend fun getByUserId(userId: Int): List<UserFilmEntity>

    @Update
    suspend fun update(userFilm: UserFilmEntity)

    @Delete
    suspend fun delete(userFilm: UserFilmEntity)

    @Query("DELETE FROM user_films")
    suspend fun clear()
}