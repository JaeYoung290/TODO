package com.example.data.notice.source.notice

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NoticeDao {
    @Query("SELECT * FROM notice")
    suspend fun getAll(): List<NoticeEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(noticeEntity: NoticeEntity)

    @Query("SELECT * FROM notice WHERE category = :category")
    suspend fun getItemsByCategory(category: String): List<NoticeEntity>
}