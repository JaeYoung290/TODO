package com.example.data.notice.repository

import com.example.data.notice.source.keyword.KeywordDao
import com.example.data.notice.source.keyword.KeywordEntity
import com.example.domain.notice.model.Keyword
import com.example.domain.notice.repository.KeywordRepository
import javax.inject.Inject

class KeywordRepositoryImpl @Inject constructor(private val keywordDao: KeywordDao) : KeywordRepository {
    override suspend fun getAllKeyword(): List<Keyword> {
        return keywordDao.getAllWords().map {
            it.toKeyword()
        }
    }

    override suspend fun insertKeyword(keyword: Keyword) {
        val keywordEntity = keyword.toEntity()
        keywordDao.insert(keywordEntity)
    }

    private fun KeywordEntity.toKeyword(): Keyword {
        return Keyword(
            id = this.id,
            keyword = this.keyword
        )
    }

    private fun Keyword.toEntity(): KeywordEntity {
        return KeywordEntity(
            id = this.id,
            keyword = this.keyword
        )
    }
}