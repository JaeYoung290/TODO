package com.example.domain.notice.repository

import com.example.domain.notice.model.Notice

interface NoticeRepository {

    suspend fun getNotices(): List<Notice>

    suspend fun insertNotice(notice: Notice)

    suspend fun getItemsByCategory(category: String): List<Notice>
}