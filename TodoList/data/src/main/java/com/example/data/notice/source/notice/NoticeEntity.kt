package com.example.data.notice.source.notice

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Notice", indices = [Index(value = ["title", "url"], unique = true)])
data class NoticeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val url: String,
    val date: String,
    val category: String,
    var isFavorite : Boolean = false,
    var isDeleted : Boolean = false
)