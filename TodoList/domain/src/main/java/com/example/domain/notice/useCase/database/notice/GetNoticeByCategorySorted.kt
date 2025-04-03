package com.example.domain.notice.useCase.database.notice

import com.example.domain.notice.model.Notice
import com.example.domain.notice.repository.NoticeRepository

class GetNoticeByCategorySorted(private val repository: NoticeRepository) {
    suspend operator fun invoke(category: String, sortField: String, sortOrder: String): List<Notice> {
        return repository.getItemsByCategorySorted(category, sortField, sortOrder)
    }
}