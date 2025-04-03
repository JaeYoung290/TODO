package com.example.domain.notice.repository

import com.example.domain.notice.model.Notice
import kotlinx.coroutines.flow.Flow

interface NoticeRepository {

    suspend fun getNotices(): List<Notice>

    suspend fun insertNotice(notice: Notice)

    suspend fun getItemsByCategory(category: String): List<Notice>

    suspend fun getItemsByCategorySorted(category: String, sortField: String, sortOrder: String): List<Notice>

    suspend fun updateDeleteStatus(itemId: Int, isDeleted: Boolean)

    suspend fun updateFavoriteStatus(itemId: Int, isFavorite: Boolean)

    suspend fun getDeletedItem(): List<Notice>

    suspend fun getFavoriteItem(): List<Notice>

    suspend fun getItemsByKeywords(category: String, keywords: List<String>): List<Notice>

}