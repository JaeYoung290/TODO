package com.example.domain.notice.useCase.database.keyword

import com.example.domain.notice.model.Keyword
import com.example.domain.notice.repository.KeywordRepository

class InsertKeyword(private val repository: KeywordRepository) {
    suspend operator fun invoke(keyword: Keyword) {
        return repository.insertKeyword(keyword)
    }
}
