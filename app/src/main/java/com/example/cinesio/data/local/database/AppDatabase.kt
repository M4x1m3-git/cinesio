package com.example.cinesio.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cinesio.data.local.dao.CommentaireDao
import com.example.cinesio.data.local.dao.FriendDao
import com.example.cinesio.data.local.dao.MovieDao
import com.example.cinesio.data.local.dao.UserDao
import com.example.cinesio.data.local.dao.UserFilmDao
import com.example.cinesio.data.local.entity.CommentaireEntity
import com.example.cinesio.data.local.entity.FriendEntity
import com.example.cinesio.data.local.entity.MovieEntity
import com.example.cinesio.data.local.entity.UserEntity
import com.example.cinesio.data.local.entity.UserFilmEntity

@Database(
    entities = [
        MovieEntity::class,
        UserEntity::class,
        UserFilmEntity::class,
        CommentaireEntity::class,
        FriendEntity::class
    ],
    version = 2,
    exportSchema = false)
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
                    "cinesio_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}