package com.example.data.notice.repository

import androidx.room.util.query
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.data.notice.source.notice.NoticeDao
import com.example.data.notice.source.notice.NoticeEntity
import com.example.domain.notice.model.Notice
import com.example.domain.notice.repository.NoticeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoticeRepositoryImpl @Inject constructor(private val noticeDao: NoticeDao) :
    NoticeRepository {
    override suspend fun getNotices(): List<Notice> {
        return noticeDao.getAll().map {
            it.toNotice()
        }
    }

    override suspend fun insertNotice(notice: Notice) {
        val noticeEntity = notice.toEntity()
        noticeDao.insert(noticeEntity)
    }

    override suspend fun getItemsByCategory(category: String): List<Notice> {
        return noticeDao.getItemsByCategory(category).map {
            it.toNotice()
        }
    }

    override suspend fun getItemsByCategorySorted(
        category: String,
        sortField: String,
        sortOrder: String
    ): List<Notice> {
        val query = createQuery(category, sortField, sortOrder)
        return noticeDao.getItemsByCategorySorted(query).map {
            it.toNotice()
        }
    }

    override suspend fun updateDeleteStatus(itemId: Int, isDeleted: Boolean) {
        noticeDao.updateDeleteStatus(itemId, isDeleted)
    }

    override suspend fun updateFavoriteStatus(itemId: Int, isFavorite: Boolean) {
        noticeDao.updateFavoriteStatus(itemId, isFavorite)
    }

    override suspend fun getDeletedItem(): List<Notice> {
        return noticeDao.getDeletedItem().map {
            it.toNotice()
        }
    }

    override suspend fun getFavoriteItem(): List<Notice> {
        return noticeDao.getFavoriteItem().map {
            it.toNotice()
        }
    }

    override suspend fun getItemsByKeywords(category: String, keywords: List<String>): List<Notice> {
        return noticeDao.getItemsByKeywords(category, keywords).map {
            it.toNotice()
        }
    }

    private fun createQuery(category: String, sortField: String = "date", sortOrder: String = "NONE"): SupportSQLiteQuery {
        val baseQuery = "SELECT * FROM notice WHERE category = '$category' AND isDeleted = 0"
        val query = when (sortOrder) {
            "ASC" -> "$baseQuery ORDER BY $sortField ASC"
            "DESC" -> "$baseQuery ORDER BY $sortField DESC"
            else -> baseQuery
        }
        return SimpleSQLiteQuery(query)
    }

    private fun Notice.toEntity(): NoticeEntity {
        return NoticeEntity(
            id = this.id,
            title = this.title,
            url = this.url,
            date = this.date,
            category = this.category,
            isFavorite = this.isFavorite,
            isDeleted = this.isDeleted
        )
    }

    private fun NoticeEntity.toNotice(): Notice {
        return Notice(
            id = this.id,
            title = this.title,
            url = this.url,
            date = this.date,
            category = this.category,
            isFavorite = this.isFavorite,
            isDeleted = this.isDeleted
        )
    }

}