package com.example.domain.notice.useCase.database.notice

import com.example.domain.notice.model.Notice
import com.example.domain.notice.repository.NoticeRepository

class GetItemsByKeywords(private val repository: NoticeRepository) {
    suspend operator fun invoke(category: String, keywords: List<String>): List<Notice> {
        return repository.getItemsByKeywords(category, keywords)
    }
}