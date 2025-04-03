package com.example.data.notice.source.keyword

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Keyword")
data class KeywordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val keyword: String
)