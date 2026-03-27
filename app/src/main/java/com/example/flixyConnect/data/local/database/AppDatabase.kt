package com.example.flixyConnect.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flixyConnect.data.local.dao.CommentaireDao
import com.example.flixyConnect.data.local.dao.FriendDao
import com.example.flixyConnect.data.local.dao.MovieDao
import com.example.flixyConnect.data.local.dao.UserDao
import com.example.flixyConnect.data.local.dao.UserFilmDao
import com.example.flixyConnect.data.local.entity.CommentaireEntity
import com.example.flixyConnect.data.local.entity.FriendEntity
import com.example.flixyConnect.data.local.entity.MovieEntity
import com.example.flixyConnect.data.local.entity.UserEntity
import com.example.flixyConnect.data.local.entity.UserFilmEntity

@Database(
    entities = [
        MovieEntity::class,
        UserEntity::class,
        UserFilmEntity::class,
        CommentaireEntity::class,
        FriendEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun userDao(): UserDao
    abstract fun userFilmDao(): UserFilmDao
    abstract fun commentaireDao(): CommentaireDao
    abstract fun friendDao(): FriendDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "flixyConnect_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}