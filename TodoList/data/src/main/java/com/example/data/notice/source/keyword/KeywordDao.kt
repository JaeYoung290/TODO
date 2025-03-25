package com.example.data.notice.source.keyword

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface KeywordDao {
    @Query("SELECT * FROM keyword")
    suspend fun getAllWords(): List<KeywordEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(keywordEntity: KeywordEntity)

    @Query("DELETE FROM keyword WHERE id = :keywordId")
    suspend fun deleteKeywordById(keywordId : Int)
}