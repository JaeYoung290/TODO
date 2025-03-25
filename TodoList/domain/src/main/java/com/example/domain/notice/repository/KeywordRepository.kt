package com.example.domain.notice.repository

import com.example.domain.notice.model.Keyword

interface KeywordRepository {

    suspend fun getAllKeyword(): List<Keyword>

    suspend fun insertKeyword(keyword: Keyword)

    suspend fun deleteKeywordById(id: Int)

}