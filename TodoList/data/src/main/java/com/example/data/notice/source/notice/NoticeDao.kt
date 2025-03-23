package com.example.data.notice.source.notice

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NoticeDao {
    @Query("SELECT * FROM notice")
    suspend fun getAll(): List<NoticeEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(noticeEntity: NoticeEntity)

    @Query("SELECT * FROM notice WHERE category = :category AND isDeleted = 0")
    suspend fun getItemsByCategory(category: String): List<NoticeEntity>

    @Query("UPDATE notice SET isDeleted = :isDeleted WHERE id = :itemId")
    suspend fun updateDeleteStatus(itemId: Int, isDeleted: Boolean)

    @Query("UPDATE notice SET isFavorite = :isFavorite WHERE id = :itemId")
    suspend fun updateFavoriteStatus(itemId: Int, isFavorite: Boolean)

    @Query("SELECT * FROM notice WHERE isDeleted = 1")
    suspend fun getDeletedItem() : List<NoticeEntity>

    @Query("SELECT * FROM notice WHERE isFavorite = 1")
    suspend fun getFavoriteItem() : List<NoticeEntity>

}