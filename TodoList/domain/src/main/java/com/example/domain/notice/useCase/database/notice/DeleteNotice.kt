package com.example.domain.notice.useCase.database.notice

import com.example.domain.notice.repository.NoticeRepository

class DeleteNotice(private val repository: NoticeRepository) {
    suspend operator fun invoke(itemId: Int, isDeleted: Boolean) {
        repository.updateDeleteStatus(itemId, isDeleted)
    }
}
