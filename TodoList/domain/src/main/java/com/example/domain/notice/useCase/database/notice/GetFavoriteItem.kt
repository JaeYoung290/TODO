package com.example.domain.notice.useCase.database.notice

import com.example.domain.notice.model.Notice
import com.example.domain.notice.repository.NoticeRepository

class GetFavoriteItem(private val repository: NoticeRepository) {
    suspend operator fun invoke(): List<Notice> {
        return repository.getFavoriteItem()
    }
}