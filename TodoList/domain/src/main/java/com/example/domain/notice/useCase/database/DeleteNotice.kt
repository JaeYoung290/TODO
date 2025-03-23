package com.example.domain.notice.useCase.database

import com.example.domain.notice.model.Notice
import com.example.domain.notice.repository.NoticeRepository

class DeleteNotice(private val repository: NoticeRepository) {
    suspend operator fun invoke(itemId: Int, isDeleted: Boolean) {
        repository.updateDeleteStatus(itemId, isDeleted)
    }
}
