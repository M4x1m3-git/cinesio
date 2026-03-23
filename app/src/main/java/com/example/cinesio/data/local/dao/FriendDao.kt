package com.example.cinesio.data.local.dao

import androidx.room.*
import com.example.cinesio.data.local.entity.FriendEntity

@Dao
interface FriendDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(friend: FriendEntity)

    @Query("SELECT * FROM friends")
    suspend fun getAllFriends(): List<FriendEntity>?

    @Query("SELECT * FROM friends WHERE friendId = :id")
    suspend fun getById(id: Int): FriendEntity?

    @Update
    suspend fun update(friend: FriendEntity)

    @Delete
    suspend fun delete(friend: FriendEntity)

    @Query("DELETE FROM friends")
    suspend fun clear()
}