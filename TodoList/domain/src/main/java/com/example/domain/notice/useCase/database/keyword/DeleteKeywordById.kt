package com.example.domain.notice.useCase.database.keyword

import com.example.domain.notice.repository.KeywordRepository

class DeleteKeywordById(private val repository: KeywordRepository) {
    suspend operator fun invoke(id: Int) {
        return repository.deleteKeywordById(id)
    }
}