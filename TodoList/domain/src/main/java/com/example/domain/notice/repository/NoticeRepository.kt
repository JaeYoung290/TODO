package com.example.domain.notice.repository

import com.example.domain.notice.model.Notice

interface NoticeRepository {

    suspend fun getNotices(): List<Notice>

    suspend fun insertNotice(notice: Notice)

    suspend fun getItemsByCategory(category: String): List<Notice>

    suspend fun updateDeleteStatus(itemId: Int, isDeleted: Boolean)

    suspend fun updateFavoriteStatus(itemId: Int, isFavorite: Boolean)

    suspend fun getDeletedItem(): List<Notice>

    suspend fun getFavoriteItem(): List<Notice>

}