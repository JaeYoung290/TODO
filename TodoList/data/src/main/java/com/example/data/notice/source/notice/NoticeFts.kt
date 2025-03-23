package com.example.data.notice.source.notice

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Fts4(contentEntity = NoticeEntity::class)
@Entity(tableName = "notice_fts")
data class NoticeFts(
    @PrimaryKey @ColumnInfo(name = "rowid") val id: Long,
    @ColumnInfo(name = "title") val title: String
)
