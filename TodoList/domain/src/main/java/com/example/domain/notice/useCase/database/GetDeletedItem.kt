package com.example.domain.notice.useCase.database

import com.example.domain.notice.model.Notice
import com.example.domain.notice.repository.NoticeRepository

class GetDeletedItem(private val repository: NoticeRepository) {
    suspend operator fun invoke(): List<Notice> {
        return repository.getDeletedItem()
    }
}