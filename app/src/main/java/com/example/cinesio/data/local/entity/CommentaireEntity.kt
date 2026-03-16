package com.example.cinesio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "commentaires",
    foreignKeys = [
        ForeignKey(
            entity = UserFilmEntity::class,
            parentColumns = ["id"],
            childColumns = ["userFilmId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userFilmId"])]
)
data class CommentaireEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userFilmId: Int,
    val rating: Float,
    val comment: String,
    val createdAt: Long
)