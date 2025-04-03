package com.example.domain.notice.useCase.core

import com.example.domain.notice.model.SortOption

class SortListUseCase {
    fun getSortByString(sortMethod: SortOption, isAsc: Boolean): String {
        val orderBy = if (isAsc) "ASC" else "DESC"
        return when (sortMethod) {
            SortOption.TITLE -> "title $orderBy"
            SortOption.DATE -> "date $orderBy"
        }
    }
}