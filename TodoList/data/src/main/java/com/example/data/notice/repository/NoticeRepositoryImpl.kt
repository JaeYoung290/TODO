package com.example.data.notice.repository

import com.example.data.notice.source.notice.NoticeDao
import com.example.data.notice.source.notice.NoticeEntity
import com.example.domain.notice.model.Notice
import com.example.domain.notice.repository.NoticeRepository
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