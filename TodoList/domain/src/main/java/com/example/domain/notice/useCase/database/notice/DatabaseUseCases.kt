package com.example.domain.notice.useCase.database.notice

data class DatabaseUseCases(
    val getNoticeByCategory: GetNoticeByCategory,
    val getAllNotices: GetAllNotices,
    val deleteNotice: DeleteNotice,
    val favoriteNotice: FavoriteNotice,
    val getDeletedItem: GetDeletedItem,
    val getFavoriteItem: GetFavoriteItem,
    val getItemsByKeywords: GetItemsByKeywords
)