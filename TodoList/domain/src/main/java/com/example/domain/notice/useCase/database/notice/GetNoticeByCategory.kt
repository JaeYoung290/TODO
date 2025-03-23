package com.example.domain.notice.useCase.database.notice

import com.example.domain.notice.model.Notice
import com.example.domain.notice.repository.NoticeRepository

class GetNoticeByCategory(private val repository: NoticeRepository) {
    suspend operator fun invoke(category: String): List<Notice> {
        return repository.getItemsByCategory(category)
    }
}