package com.example.domain.notice.useCase.database.notice

import com.example.domain.notice.repository.NoticeRepository

class FavoriteNotice(private val repository: NoticeRepository) {
    suspend operator fun invoke(itemId: Int, isFavorite: Boolean) {
        repository.updateFavoriteStatus(itemId, isFavorite)
    }
}
