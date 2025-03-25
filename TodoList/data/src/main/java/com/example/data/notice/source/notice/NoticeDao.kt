package com.example.data.notice.source.notice

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.domain.notice.model.Keyword
import com.example.domain.notice.model.Notice
import kotlinx.coroutines.flow.Flow

@Dao
interface NoticeDao {
    @Query("SELECT * FROM notice")
    suspend fun getAll(): List<NoticeEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(noticeEntity: NoticeEntity)

    @Query("SELECT * FROM notice WHERE category = :category AND isDeleted = 0")
    suspend fun getItemsByCategory(category: String): List<NoticeEntity>

    @RawQuery
    suspend fun getItemsByCategorySorted(query: SupportSQLiteQuery): List<NoticeEntity>

    @Query("UPDATE notice SET isDeleted = :isDeleted WHERE id = :itemId")
    suspend fun updateDeleteStatus(itemId: Int, isDeleted: Boolean)

    @Query("UPDATE notice SET isFavorite = :isFavorite WHERE id = :itemId")
    suspend fun updateFavoriteStatus(itemId: Int, isFavorite: Boolean)

    @Query("SELECT * FROM notice WHERE isDeleted = 1")
    suspend fun getDeletedItem(): List<NoticeEntity>

    @Query("SELECT * FROM notice WHERE isFavorite = 1 AND isDeleted = 0")
    suspend fun getFavoriteItem(): List<NoticeEntity>

    @Query("SELECT * FROM notice JOIN notice_fts ON notice.id == notice_fts.rowid WHERE notice_fts.title LIKE :keyword AND notice.category = :category AND notice.isDeleted = 0")
    suspend fun getItemsByKeyword(category: String, keyword: String): List<NoticeEntity>

    @Transaction
    suspend fun getItemsByKeywords(category: String, keywords: List<String>): List<NoticeEntity> {
        val result = mutableListOf<NoticeEntity>()
        for (keyword in keywords) {
            result.addAll(getItemsByKeyword(category, keyword))
        }
        return result.distinctBy { it.id }
    }
}